// components/NewPlayer.js

import React, { useState } from 'react';
import fire from "../config/fire-config";

const NewPlayer = () => {
    const [name, setName] = useState('');
    const [notification, setNotification] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();

        fire.firestore()
            .collection('users')
            .add({
                name: name
            });

        console.log({
            "name": name
        });

        setName('');

        setNotification('Name set');

        setTimeout(() => {
            setNotification('')
        }, 2000)

        fire.auth().signInAnonymously()
            .then(() => {
                // Signed in..

            })
            .catch((error) => {
                const errorCode = error.code;
                const errorMessage = error.message;
                // ...
            })
    }

    return (
        <div>
            <h2>Add User</h2>
            {notification}
            <form onSubmit={handleSubmit}>
                <div>
                    Set Name<br />
                    <input type="text" value={name}
                           onChange={({target}) => setName(target.value)} />
                </div>
                <button type="submit">Save</button>
            </form>
        </div>
    )
}

export default NewPlayer;