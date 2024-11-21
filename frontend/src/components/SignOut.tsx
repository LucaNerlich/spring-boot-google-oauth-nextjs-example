'use client';

import React from 'react';
import {useRouter} from 'next/navigation'

export default function SignOut(): React.ReactElement {
    const router = useRouter()

    async function handleSignOut() {
        await fetch(`${process.env.NEXT_PUBLIC_API_URL}/logout`, {
            method: 'POST',
            credentials: 'include',
        });

        // redirect to home
        router.push("/");
    }

    return (
        <button onClick={() => handleSignOut()}>
            Logout
        </button>
    )
}
