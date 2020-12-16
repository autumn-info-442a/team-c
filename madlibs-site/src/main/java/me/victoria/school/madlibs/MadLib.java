package me.victoria.school.madlibs;

import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.impl.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MadLib implements Comparable<MadLib> {
    public static String jsonText = "{\n" +
            "  \"madlibs\": [\n" +
            "    \"Recess Rules with Garfield\\nHey kids! Garfield here. Before we go to recess, here are some rules:\\n1. Recess is {ml:text:Number} minutes long.\\n2. Never start a {ml:text:Food} in the {ml:text:Type of food}.\\n3. Don’t {ml:text:Verb} too fast!\\n4. Four square is for {ml:text:Number} players only.\\n5. You can {ml:text:Verb} with your teachers too!\\n6. Put the {ml:text:Plural noun} back in the box before {ml:text:Number}\\n7. Don’t forget to have a {ml:text:Adjective} time!\",\n" +
            "    \"Disneyland\\nYesterday, my friends and I {ml:text:Adverb} went to Disneyland after\\n{ml:text:Verb ending in -ing} our parents to go for {ml:text:Length of time}.\\nThe tickets were more expensive than my parents had imagined, but\\nthankfully, they {ml:text:Past tense verb} for it anyway. When we\\nentered Disneyland, we received a {ml:text:Adjective} stamp on our left\\nhand. After entering the premises, we took a lot of pictures in front of the\\nMickey Mouse shaped grass. We then proceeded to go on {ml:text:Number}\\nrides, including Autopia, Splash Mountain, Space Mountain, and the\\nMatterhorn. Towards the end of the day,we got to see the {ml:text:Noun}\\nthat had all the Disney characters, including my favorite, Pluto. After\\ncoming home, I got in trouble for {ml:text:Verb ending in -ing} too much\\n{ml:text:Food}, but was able to cherish the memories we made, including\\ntaking {ml:text:Noun} with Goofy, Donald, Woody, Mulan, and Elsa. That\\nnight, I fell asleep almost {ml:text:Adverb} with the plush Tigger\\npillow pet I {ml:text:Past tense verb}.\",\n" +
            "    \"Thanksgiving\\nI woke up to the {ml:text:Adjective} smell of roasted turkey that my mom\\nwas {ml:text:Verb ending in -ing} in the kitchen. Today is Thanksgiving day, and my\\nparents, my brother, and I are going over to our grandparents’ house for\\ndinner. Mom is making turkey along with {ml:text:Number} mashed potatoes\\nand gravy for the occasion. After {ml:text:Adverb} brushing my teeth, I\\nwent downstairs to look at the turkey. For me, the {ml:text:Noun} is what\\nmakes Thanksgiving holiday my favorite. After {ml:text:Verb ending in -ing}\\nall day, we finally got ready to go over to my grandparents’ house. When\\nwe got there, the table was filled with food, from pumpkin and apple pie\\nto butternut squash soup to {ml:text:Food} - we had it all. After Mom set\\nthe turkey, mashed potatoes, and gravy on the table, we ate and talked\\naway until midnight. {ml:text:Adjective}. Thanksgiving. Ever.\",\n" +
            "    \"Crazy Basketball Game\\nHello, everyone! This is {ml:proper:Person}, speaking to you from the\\nbroadcasting {ml:text:Noun} at the {ml:text:adjective} forum. In case you\\n{ml:text:Past tense verb} in late, the score between the Seattle\\n{ml:proper:Plural noun} and the Boston {ml:proper:Plural noun} is wild,\\n122 to {ml:text:Number}. This has been the most {ml:text:Adjective} game\\nthese {ml:text:Adjective} eyes have seen in years. First, one team scores\\na {ml:text:Noun}, then {ml:text:Exclamation}! – the other team comes\\nright back. Okay, time-out is over.  Seattle brings in the ball at\\nmid-{ml:text:Noun}. {ml:proper:Person} dribbles down the {ml:text:Noun},\\nfakes the defender out of his {ml:text:Noun} and shoots a {ml:text:Number}-handed\\nshot. It goes right through the {ml:text:Noun}. He beat the {ml:text:Noun}!\\nThe game is over just as the {ml:text:Noun} goes off.\",\n" +
            "    \"How to Talk Like a Pirate\\nYe can always pretend to be a bloodthirsty {ml:text:Noun}, threatening\\neveryone by waving yer {ml:text:Adjective} sword in the air, but until ye\\nlearn to {ml:text:Verb} like a pirate, ye’ll never be accepted as an\\nauthentic {ml:text:Noun}. So here’s what ye do: Cleverly work into yer\\ndaily conversations {ml:text:Adjective} pirate phrases such as “Ahoy\\nthere, {ml:text:Noun}, “Avast, ye {ml:text:Noun}, and “Shiver me\\nml:text:Plural noun}“. Remember to drop all yer gs when ye say such\\nwords as \\\"sailin’\\\", \\\"spittin’\\\", and \\\"fightin’\\\". This will give\\nye a/an {ml:text:Part of the body} start to being recognized as a\\nswashbucklin’ {ml:text:Noun}. Once ye have the lingo down pat, it helps\\nto wear a three-cornered {ml:text:Noun} on yer head and keep a/an\\n{ml:text:Noun} perched atop yer {ml:text:Part of the body}. Aye, now ye\\nbe a real pirate!\"\n" +
            "  ]\n" +
            "}";
    public static JsonArray madlibs = ((JsonObject)JsonUtil.parse(jsonText)).getArray("madlibs");
    public static List<MadLib> madLibs = new ArrayList<>();
    private String name;
    private String text;
    private Pattern matcher = Pattern.compile("\\{(.*?)}");
    public MadLib(String jsonText){
        String[] lines = jsonText.split("\\n");
        String[] copy = new String[lines.length - 1];
        System.arraycopy(lines, 1, copy, 0, lines.length - 1);
        text = String.join("\n", copy);
        name = lines[0];
    }

    public MadLib(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getNextFill() {
        Matcher match = matcher.matcher(text);
        if (match.find()) {
            return match.group();
        } else {
            return null;
        }
    }

    public boolean submit(String match, String replace) {
        text = text.replaceFirst("\\{(.*?)}", replace);
        return getNextFill() != null;
    }

    @Override
    public int compareTo(MadLib o) {
        return getName().compareTo(o.getName());
    }
}
