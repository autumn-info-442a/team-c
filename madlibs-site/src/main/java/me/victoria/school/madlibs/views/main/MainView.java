package me.victoria.school.madlibs.views.main;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import me.victoria.school.madlibs.ActivityTracker;
import me.victoria.school.madlibs.Broadcaster;
import me.victoria.school.madlibs.MadLib;
import me.victoria.school.madlibs.ProfanityCheck;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.vaadin.olli.ClipboardHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@JsModule("./styles/shared-styles.js")
@PageTitle("MadLibs")
@CssImport("./styles/views/main/main-view.css")
@Route("")
public class MainView extends VerticalLayout {
    private H1 viewTitle;
    private List<Component> currentlyShowing = new ArrayList<>();
    private VerticalLayout layout = new VerticalLayout();
    private Registration broadcastRegistration;
    private String name;
    private String lobby;
    private boolean isHost = false;
    private int playerId;
    private int playerCount;
    private String madlibName;
    private MadLib madlib;
    private UUID uuid;
    private int nextPlayerId;
    private int currentPlayerTurn = 0;
    private String wordType;

    private void resetVariables() {
        wordType = null;
        currentPlayerTurn = 0;
        isHost = false;
        nextPlayerId = -1;
        madlib = null;
        madlibName = null;
        playerCount = 0;
        playerId = 0;
        lobby = null;
    }

    public MainView() {
        uuid = UUID.randomUUID();
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();
        themeList.add(Lumo.DARK);
        H1 welcome = new H1("Welcome to Mad Libs!");
        setAlignItems(Alignment.CENTER);
        layout.setAlignItems(Alignment.CENTER);
        add(welcome, layout);
        login();
        push();
    }

    @Override
    protected void onAttach(AttachEvent event) {
        UI ui = event.getUI();
        broadcastRegistration = Broadcaster.register(s -> handleBroadcast(ui, s));
    }

    @Override
    protected void onDetach(DetachEvent event) {
        broadcastRegistration.remove();
        broadcastRegistration = null;
    }

    private void handleBroadcast(UI ui, String message) {
        if (name == null || lobby == null) {
            return;
        }
        try {
            String[] lines = message.split("\\|");
            if (!lobby.equals(lines[0])) {
                return;
            }
            switch (lines[1]) {
                case "ERROR":
                    error(lines[2], false, 10000);
                    break;
                case "SHUTDOWN_INACTIVITY":
                    resetVariables();
                    ui.access((Command) () -> {
                        error("The lobby you were in has been shutdown due to inactivity for more than 20 minutes", false, 10000);
                        mainMenu();
                        push();
                    });

                    break;
                case "SHUTDOWN_HOSTCLOSED":
                    resetVariables();
                   ui.access((Command) () -> {
                       error("The lobby you were in has been shutdown due to the host closing the game", false, 10000);
                       mainMenu();
                       push();
                   });
                    break;
                case "PLAYER_JOIN_ATTEMPT":
                    if (!isHost) {
                        return;
                    }
                    if (playerCount >= 4) {
                        Broadcaster.broadcast(lobby + "|" + "PLAYER_JOIN_ATTEMPT_REJECT" + "|" + lines[2] + "|This lobby is already full");
                        return;
                    }
                    playerCount++;
                    Broadcaster.broadcast(lobby + "|PLAYER_JOIN_ATTEMPT_ACCEPT|" + lines[2] + "|" + nextPlayerId++);
                    break;
                case "PLAYER_JOIN_ATTEMPT_REJECT":
                    if (isHost) {
                        return;
                    }
                    if (!lines[2].equals(uuid.toString())) {
                        return;
                    }
                    lobby = "";
                    ui.access((Command) () -> {
                        mainMenu();
                        error(lines[3], false, 10000);
                        push();
                    });
                    break;
                case "PLAYER_JOIN_ATTEMPT_ACCEPT":
                    if (isHost) {
                        return;
                    }
                    if (!lines[2].equals(uuid.toString())) {
                        return;
                    }
                    playerId = Integer.parseInt(lines[3]);
                    Broadcaster.broadcast(lobby + "|PLAYER_JOINED|" + uuid.toString() + "|" + name);
                    ui.access((Command) () -> {
                        waitingLobby();
                        push();
                    });
                    break;
                case "PLAYER_JOINED":
                    if (lines[2].equals(uuid.toString())) {
                        return;
                    }
                    ui.access((Command) () -> {
                        Notification.show(lines[3] + " has joined the lobby!", 5000, Notification.Position.MIDDLE);
                        push();
                    });
                    return;
                case "START_GAME":
                    madlibName = lines[2];
                    currentPlayerTurn = Integer.parseInt(lines[3]);
                    wordType = lines[4];
                    ui.access((Command) this::runGame);
                    return;
                case "SUBMIT_WORD":
                    if (!isHost) {
                        return;
                    }
                    String resp = lines[3];
                    String type = lines[2];
                    String[] typeSplit = wordType.replace("{", "").replace("}", "").split(":");
                    if (typeSplit[1].equals("proper")) {
                        resp = WordUtils.capitalizeFully(resp);
                    } else {
                        resp = resp.toLowerCase();
                    }
                    if (madlib.submit(type, resp)) {
                        currentPlayerTurn++;
                        if (currentPlayerTurn >= nextPlayerId) {
                            currentPlayerTurn = 1;
                        }
                        Broadcaster.broadcast(lobby + "|START_GAME|" + madlibName + "|" + currentPlayerTurn + "|" + madlib.getNextFill());
                    } else {
                        Broadcaster.broadcast(lobby + "|WAIT_HOST");
                        ui.access((Command) this::hostFinishGame);
                    }
                    break;
                case "WAIT_HOST":
                    if (isHost) {
                        return;
                    }
                    ui.access((Command) this::waitForHost);
                    break;
                case "FINISH_GAME":
                    ui.access((Command) () -> results(lines[2]));
                    break;
                case "RESET_LOBBY":
                    ui.access((Command) () -> {
                        if (isHost) {
                            createLobby(true);
                        } else {
                            waitingLobby();
                        }
                    });
            }
        } catch (Exception e) {
            System.err.println(name + ": " + ExceptionUtils.getStackTrace(e));
        }
    }

    public void push() {
        UI.getCurrent().push();
    }

    public void clear() {
        currentlyShowing.forEach(c -> layout.remove(c));
        currentlyShowing.clear();
        push();
    }

/*    public void clear(Component component) {
        currentlyShowing.remove(component);
        layout.remove(component);
        push();
    }*/

    public void display(Component... components) {
        Arrays.stream(components).forEach(component -> {
            currentlyShowing.add(component);
            layout.add(component);
        });
        push();
    }

    public void error(String message, boolean permanent, int duration) {
        Notification error = new Notification(message);
        error.setPosition(Notification.Position.MIDDLE);
        if (!permanent) {
            error.setDuration(duration);
        }
        error.addThemeVariants(NotificationVariant.LUMO_ERROR);
        error.open();
        push();
    }

    /**
     * Various views
     */
    private void login() {
        clear();
        HorizontalLayout hl = new HorizontalLayout();
        TextField name = new TextField("Your name");
        name.setMaxLength(18);
        name.setMinLength(3);
        name.setPlaceholder("John");
        Button submit = new Button("Submit");
        hl.add(name, submit);
        hl.setVerticalComponentAlignment(FlexComponent.Alignment.END, name, submit);
        submit.addClickListener(e -> {
            if (name.getValue().length() < 3) {
                name.setInvalid(true);
            }
            if (name.isInvalid()) {
                error("Provided name is invalid. Must be between 3 and 18 characters.", false, 10000);
                return;
            }
            Notification.show("Welcome, " + name.getValue() + "!", 5000, Notification.Position.BOTTOM_CENTER);
            this.name = name.getValue();
            mainMenu();
        });
        display(hl);
        push();
    }

    private void mainMenu() {
        clear();
        Text start = new Text("Starting a new game? Generate a game code:");
        Button startButton = new Button("GIVE ME A CODE!");
        startButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> createLobby(false));
        Text join = new Text("Lobby already created? Enter an existing game code:");
        HorizontalLayout hl = new HorizontalLayout();
        TextField code = new TextField();
        code.setPlaceholder("ABCD-WXYZ-1234");
        Button submit = new Button("Join");
        submit.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            if (!ActivityTracker.isValidCode(code.getValue())) {
                error("That is not a valid lobby code", false, 5000);
                return;
            }
            lobby = code.getValue();
            if (ActivityTracker.hasGameStarted(lobby)) {
                error("That lobby has already started", false, 5000);
            }
            Broadcaster.broadcast(lobby + "|PLAYER_JOIN_ATTEMPT|" + uuid.toString());
        });
        hl.add(code, submit);
        display(start, startButton, join, hl);
        push();
    }

    private void createLobby(boolean reset) {
        clear();
        if (!reset) {
            lobby = ActivityTracker.generateLobbyCode();
            playerId = 1;
            isHost = true;
            nextPlayerId = 2;
            playerCount = 1;
        }
        HorizontalLayout hz = new HorizontalLayout();
        Html p = new Html("<p>Lobby Code: <code>" + lobby + "</code></p>");
        Button copy = new Button("Copy");
        ClipboardHelper clipboardHelper = new ClipboardHelper(lobby, copy);
        hz.add(p, clipboardHelper);
        Paragraph p2 = new Paragraph("Give this to your friends so they can join you!");
        Paragraph p3 = new Paragraph("Once everyone's in, choose a MadLib from the dropdown menu and click start!");
        Select<MadLib> dropdown = new Select<>();
        dropdown.setLabel("MadLib");
        dropdown.setItemLabelGenerator(MadLib::getName);
        dropdown.setItems(MadLib.madLibs);
        Button start = new Button("Start");
        ConfirmDialog confirm = new ConfirmDialog("Start Game", "Are you sure you want to start the game with only a partially filled lobby? You can play with up to four players!", "Yes", (ComponentEventListener<ConfirmDialog.ConfirmEvent>) confirmEvent -> {
/*            clear(start);
            push();*/
            madlib = new MadLib(dropdown.getValue().getName(), dropdown.getValue().getText());
            ActivityTracker.startLobby(lobby);
            Broadcaster.broadcast(lobby + "|START_GAME|" + madlib.getName() + "|" + playerId + "|" + madlib.getNextFill());
        }, "No", (ComponentEventListener<ConfirmDialog.CancelEvent>) cancelEvent -> Notification.show("Cancelled", 1500, Notification.Position.MIDDLE));
        start.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            if (dropdown.getValue() == null) {
                error("You haven't chosen a MadLib", false, 5000);
                return;
            }
            if (nextPlayerId < 5) {
                confirm.open();
            } else {
/*                clear(start);
                push();*/
                madlib = new MadLib(dropdown.getValue().getName(), dropdown.getValue().getText());
                ActivityTracker.startLobby(lobby);
                Broadcaster.broadcast(lobby + "|START_GAME|" + madlib.getName() + "|" + playerId + "|" + madlib.getNextFill());
            }
        });
        HorizontalLayout hz2 = new HorizontalLayout();
        hz2.add(dropdown, start);
        display(hz, p2, p3, hz2, confirm);
        push();
    }

    private void runGame() {
        clear();
        H2 h2 = new H2("Currently playing:");
        Html header2 = new Html("<b>" + madlibName + "</b>");
        if (currentPlayerTurn != playerId) {
            Paragraph p = new Paragraph("Just a moment! Your friend Player " + currentPlayerTurn + " is typing in their word. You'll get your chance any second!");
            display(h2, header2, p);
        } else {
            String type = wordType.replace("{", "").replace("}", "").split(":")[2];
            Text dialog = new Text("Please enter a " + type + ":");
            TextField prompt = new TextField();
            prompt.setMinLength(2);
            prompt.setMaxLength(25);
            Button submit = new Button("Submit");
            submit.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
                if (prompt.isInvalid() || prompt.getValue().length() == 0) {
                    error("Invalid input", false, 3000);
                    return;
                }
                if (ProfanityCheck.isBadWord(prompt.getValue())) {
                    error("No swearing", false, 3000);
                    return;
                }
                //clear(submit);
                //push();
                Broadcaster.broadcast(lobby + "|SUBMIT_WORD|" + wordType + "|" + prompt.getValue());
            });
            HorizontalLayout hz = new HorizontalLayout();
            hz.add(prompt, submit);
            display(h2, header2, dialog, hz);
        }
    }

    private void waitingLobby() {
        clear();
        H2 h2 = new H2("Waiting for the host to start the lobby");
        Paragraph p = new Paragraph("Your Player ID is: " + playerId);
        display(h2, p);
        push();
    }

    private void hostFinishGame() {
        clear();
        H2 h2 = new H2("Currently playing:");
        Html header2 = new Html("<b>" + madlibName + "</b>");
        Paragraph p = new Paragraph("Awesome! You've finished the Mad Lib. Ready to see it?");
        Button finish = new Button("Finish");
        finish.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            //clear(finish);
            push();
            Broadcaster.broadcast(lobby + "|FINISH_GAME|" + madlib.getText());
        });
        display(h2, header2, p, finish);
    }

    private void waitForHost() {
        clear();
        H2 h2 = new H2("Currently playing:");
        Html header2 = new Html("<b>" + madlibName + "</b>");
        Paragraph p = new Paragraph("Please wait for the host to finalize the game");
        display(h2, header2, p);
    }

    private void results(String text) {
        clear();
        H2 h2 = new H2("Read it aloud to each other!");
        Html header2 = new Html("<b>" + madlibName + "</b>");
        Html madlibText = new Html("<span style=\"white-space: pre-line\">" + text + "</span>");
        display(h2, header2, madlibText);
        HorizontalLayout hz = new HorizontalLayout();
        if (isHost) {
            Button reset = new Button("Play Again");
            reset.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
                ActivityTracker.backInLobby(lobby);
                Broadcaster.broadcast(lobby + "|RESET_LOBBY");
            });
            Button exit = new Button("Exit Game");
            exit.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
                ActivityTracker.stopLobby(lobby);
                Broadcaster.broadcast(lobby+"|SHUTDOWN_HOSTCLOSED");
            });
            hz.add(reset, exit);
            display(hz);
        }

    }


}
