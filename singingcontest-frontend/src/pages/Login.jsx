import React from "react";
import { Link } from "react-router-dom";
import GoogleIcon from "../assets/social-icons/google.png";
import KakaoIcon from "../assets/social-icons/kakao.png";
import GithubIcon from "../assets/social-icons/github.png";


function Login() {
    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-50">
            <div className="bg-white p-8 rounded-lg shadow-lg w-96">
                <h2 className="text-2xl font-bold mb-4 text-center">Log In</h2>
                <form className="space-y-4">
                    <input
                      type="email"
                      placeholder="이메일"
                      className="w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <input
                      type="password"
                      placeholder="패스워드"
                      className="w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <button className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition">
                        Log In
                    </button>
                </form>
                <p className="mt-4 text-sm text-gray-500">
                    Don't have an account?{" "}
                    <Link to="/signup" className="text-blue-600 hover:underline">
                        Sign Up
                    </Link>
                </p>
            </div>
        </div>
    )
}

export default Login;