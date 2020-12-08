import { useState, useEffect } from 'react';
import Head from 'next/head';
import fire from '../config/fire-config';
import Link from 'next/link';
import styles from '../styles/Home.module.css'
import Game from '../components/Game.js'

const Home = () => {
    const [users, setUsers] = useState([]);

    useEffect(() => {
        fire.firestore()
            .collection('users')
            .onSnapshot(snap => {
                const users = snap.docs.map(doc => ({
                    id: doc.id,
                    ...doc.data()
                }));
                setUsers(users);
            });
    }, []);

    console.log(users);

  return (
      <div>
        <Head>
          <title>Mad Libs</title>
        </Head>
        <h1>Mad Libs</h1>
          <Link href="/setup/login">
              <a> Login</a>
          </Link>
      </div>
  )
}
export default Home;
