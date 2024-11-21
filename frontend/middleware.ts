import {NextRequest, NextResponse} from 'next/server'
import {verifySession} from "@/data-access-layer";
import {cookies} from "next/headers";

// 1. Specify protected and public routes
const publicRoutes = ['/public']

// https://nextjs.org/docs/app/building-your-application/authentication#optimistic-checks-with-middleware-optional
export default async function middleware(req: NextRequest) {
    // 2. Check if the current route is protected or public
    const path = req.nextUrl.pathname
    const isPublicRoute = path === "/" || publicRoutes.some(route => {
        return path.startsWith(route);
    });

    // 3. Check potentially invalid session
    const isAuth = await verifySession();

    // 4. Early return if public route
    if (isPublicRoute) {
        return NextResponse.next()
    }

    // 5. Redirect to home if the user is not authenticated
    const isProtectedRoute = !publicRoutes.includes(path)
    if (isProtectedRoute && !isAuth) {
        const cookieStore = await cookies();
        console.warn("User not authenticated or banned. Removing session cookie. Redirect to SignIn.");
        cookieStore.delete('JSESSIONID');
        cookieStore.delete('XSRF_TOKEN');

        // redirect to sign-in
        return NextResponse.redirect(`${process.env.NEXT_PUBLIC_API_URL}/oauth2/authorization/google`);
    }

    // 6. Continue
    return NextResponse.next()
}

// Routes Middleware should not run on
export const config = {
    matcher: ['/((?!api|_next/static|_next/image|icons|favicon.ico|public|.*\\.png$).*)'],
}
