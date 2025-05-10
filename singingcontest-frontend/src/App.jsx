import React from "react";
import "./index.css";
import Header from "./components/Header";
import Footer from "./components/Footer";

function App() {
  return (
    <div className="flex flex-col min-h-screen">
      <Header />
      <main className="flex flex-col items-center justify-center flex-grow bg-gradient-to-b from-blue-50 to-white">
        <div className="text-center p-10">
          <h1 className="text-5xl font-extrabold text-blue-600 mb-4">
            Singing Contest 🎤
          </h1>
          <p className="text-lg text-gray-700 mb-6">
            Share your voice with the world!
          </p>
          <button className="bg-blue-500 text-white px-8 py-3 rounded-full text-lg font-semibold hover:bg-blue-600 transition duration-200">
            Get Started
          </button>
        </div>
      </main>
      <Footer />
    </div>
  );
}

export default App;
