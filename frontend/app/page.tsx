import React from "react";
import {verifySession} from "@/data-access-layer";
import Link from "next/link";
import SignOut from "@/components/SignOut";

export default async function Home() {
    const isAuth = await verifySession();

    return (
        <div>
            <p>Render Login (when logged out)</p>
            {!isAuth &&
                <Link href={`${process.env.NEXT_PUBLIC_API_URL}/oauth2/authorization/google`}>
                    <button>
                        Login with Google
                    </button>
                </Link>
            }

            <p>Render Logout (when logged in)</p>
            {isAuth && <SignOut/>}
        </div>
    );
}
