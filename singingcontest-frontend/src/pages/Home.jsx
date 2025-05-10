import React from "react";

function Home() {
  return (
    <div className="flex flex-col items-center justify-center text-center p-10">
      <h1 className="text-4xl font-bold mb-4 text-blue-600">Singing Contest 🎤</h1>
      <p className="text-lg mb-6 text-gray-700">
        Share your voice with the world!
      </p>
      <button className="bg-blue-500 text-white px-6 py-2 rounded-full hover:bg-blue-600 transition">
        Get Started
      </button>
    </div>
  );
}

export default Home;
