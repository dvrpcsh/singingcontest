// src/App.jsx
import React from 'react';
import VideoFeed from './components/VideoFeed';
import Sidebar from './components/Sidebar';

export default function App() {
  return (
    <div className="flex h-screen">
      <main className="flex-1 overflow-auto bg-gray-100 p-4">
        <VideoFeed />
      </main>
      <aside className="w-1/4 border-l bg-white p-4">
        <Sidebar />
      </aside>
    </div>
  );
}
