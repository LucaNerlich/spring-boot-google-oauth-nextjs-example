import React from "react";
import Link from "next/link";

export default function Page() {
    return (
        <div className="d-flex justify-content-center align-items-center vh-100">
            <div className="text-center">
                <h1 className="display-1">404</h1>
                <p className="lead">Page Not Found</p>
                <br/>
                <Link href="/">
                    <button className="btn btn-primary">Go Home</button>
                </Link>
            </div>
        </div>
    );
}
