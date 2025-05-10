import React from "react";
import { Link } from "react-router-dom";

function Header() {
  return (
    <header className="bg-gray-800 text-white p-4 flex justify-between items-center">
      <Link to="/" className="text-2xl font-bold">Singing Contest</Link>
      <nav>
        <Link to="/" className="mr-4">Home</Link>
        <Link to="/login" className="mr-4">Login</Link>
        <Link to="/signup" className="bg-blue-500 px-4 py-2 rounded-full hover:bg-blue-600 transition">
          Sign Up
        </Link>
      </nav>
    </header>
  );
}

export default Header;
