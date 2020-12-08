import Game from "../../components/Game";
import Head from "next/head";
import Link from "next/link";


const JoinGame = () => {
    return (
        <div>
            <Head>
                <title>Mad Libs</title>
            </Head>
            <h1>Mad Libs</h1>
            <Link href="/game/gamelobby">
                <a>Join Game</a>
            </Link>
        </div>
    )
}

export default JoinGame;
