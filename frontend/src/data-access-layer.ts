import 'server-only'

import {cookies} from 'next/headers'
import {AuthenticatedAccount} from "@/types/account/AuthenticatedAccount";

// https://nextjs.org/docs/app/building-your-application/authentication#creating-a-data-access-layer-dal
export const verifySession = (async () => {
    const cookieStore = await cookies();

    async function getUser() {
        const authResponse = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/auth/me`, {
            method: 'GET',
            headers: {
                'Cookie': `JSESSIONID=${cookieStore.get('JSESSIONID')?.value}`
            }
        });
        return await authResponse?.json();
    }

    if (cookieStore.has('JSESSIONID') && cookieStore.get('JSESSIONID')?.value != '') {
        const account: AuthenticatedAccount = await getUser();
        console.log("account", account);

        return !(!account.id || account.banned);
    }
})
