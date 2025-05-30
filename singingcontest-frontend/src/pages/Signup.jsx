import React,{ useState,useEffect } from "react";
import { Link,useNavigate } from "react-router-dom";
import GoogleIcon from "../assets/social-icons/google.png";
import KakaoIcon from "../assets/social-icons/kakao.png";
import GithubIcon from "../assets/social-icons/github.png";

function Signup() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();


    const handleSignup = async () => {
        const response = await fetch("http://localhost:8080/api/auth/signup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                name: name,
                email: email,
                password: password
            })
        });

        if(response.ok) {
            alert("회원가입 성공!");
            navigate("/login");
        } else {
            const message = await response.text();
            alert(`회원가입 실패: ${message}`);
        }
    }

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-50">
            <div className="bg-white p-8 rounded-lg shadow-lg w-96">
                <h2 className="text-2xl font-bold mb-4 text-center">Sign Up</h2>
                <form className="space-y-4">
                    <input
                      type="text"
                      placeholder="이름"
                      className="w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <input
                      type="email"
                      placeholder="이메일"
                      className="w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <input
                      type="password"
                      placeholder="비밀번호"
                      className="w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <button className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition">
                        Sign Up
                    </button>
                </form>
                <div className="mt-4 text-center">
                    <p className="text-sm text-gray-500">Or Sign up with</p>
                    <div className="flex items-center justify-center mt03 space-x-4">
                        <img src={GoogleIcon} alt="Google" className="w-8 h-8 cursor-pointer" />
                        <img src={KakaoIcon} alt="Kakao" className="w-8 h-8 cursor-pointer" />
                        <img src={GithubIcon} alt="Github" className="w-8 h-8 cursor-pointer" />
                    </div>
                    <p className="mt04 text0sm text-gray-500">
                        Already have an account?{" "}
                        <Link to="/login" className="text-blue-600 hover:underline">
                            Login
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    )
}

export default Signup;