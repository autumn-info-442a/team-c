import Link from 'next/link'

export default function Lobby() {
    return (
        <>
            <h1>Lobby</h1>
            <h2>
                <Link href="/">
                    <a>Back to home</a>
                </Link>
            </h2>
        </>
    )
}