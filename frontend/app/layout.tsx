import type {Metadata} from "next";
import "@/styles/globals.css";
import "@/styles/resets.css";
import React from "react";

export const metadata: Metadata = {
    title: "",
    description: "",
    applicationName: "",
    creator: "",
    keywords: "",
};

export default async function RootLayout({children,}: Readonly<{ children: React.ReactNode; }>) {
    return (
        <html lang="de">
        <head>
            <title>spring-boot-google-oauth-nextjs-example-frontend</title>
        </head>
        <body>
        <header></header>
        <main>
            {children}
        </main>
        <footer></footer>
        </body>
        </html>
    );
}
