'use server'

import {cookies} from "next/headers";
import {NextResponse} from "next/server";

export async function GET() {
    const cookieStore = await cookies();
    await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/auth/logout`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Cookie': `JSESSIONID=${cookieStore.get('JSESSIONID')?.value}`
        },
    })

    // https://nextjs.org/docs/app/api-reference/functions/cookies#deleting-cookies
    cookieStore.set('JSESSIONID', '', {maxAge: 0});
    cookieStore.set('XSRF_TOKEN', '', {maxAge: 0});

    return NextResponse.redirect(process.env.NEXT_PUBLIC_URL ?? "");
}
