"use client";

import React from "react";
import Link from "next/link";

export default function Page() {
    return (
        <div>
            <h1>Error</h1>
            <p>Something went wrong. Please try again later.</p>
            <Link href="/">
                <button>Go Home</button>
            </Link>
        </div>
    );
}
