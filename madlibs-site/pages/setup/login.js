import NewPlayer from "../../components/NewPlayer";
import fire from "../../config/fire-config";
import { useRouter } from 'next/router';

const Login = () => {
    const router = useRouter();

    fire.auth().onAuthStateChanged((user) => {
        if (user) {
            // User is signed in, see docs for a list of available properties
            // https://firebase.google.com/docs/reference/js/firebase.User
            const uid = user.uid;
            console.log(uid);
            router.push("/setup/joingame")
            // ...
        } else {
            // User is signed out
            // ...
        }
    });

    return (
        <NewPlayer/>
    )
}

export default Login;
