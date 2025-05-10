import React from "react";
import { Link } from "react-router-dom";

function Header() {
  return (
    <header className="bg-white shadow-md p-4 flex justify-between items-center">
      <Link to="/" className="text-2xl font-bold text-blue-600">Singing Contest</Link>
      <nav className="flex gap-4">
        <Link to="/" className="text-blue-600 hover:underline">Home</Link>
        <Link to="/login" className="text-blue-600 hover:underline">Login</Link>
        <Link to="/signup" className="px-4 py-2 bg-blue-500 text-white rounded-full hover:bg-blue-600 transition">
          Sign Up
        </Link>
      </nav>
    </header>
  );
}

export default Header;
