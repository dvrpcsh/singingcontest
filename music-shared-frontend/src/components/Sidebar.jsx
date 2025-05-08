// src/components/Sidebar.jsx
import React from 'react';

const weeklyTop = [
  { id: 1, title: '인기 영상 A' },
  { id: 2, title: '인기 영상 B' },
  // ...
];
const monthlyTop = [
  { id: 1, title: '월간 영상 A' },
  { id: 2, title: '월간 영상 B' },
  // ...
];

export default function Sidebar() {
  return (
    <div>
      <h2 className="text-lg font-bold mb-2">주간 TOP5</h2>
      <ul className="space-y-1 mb-4">
        {weeklyTop.map((item) => (
          <li key={item.id} className="text-sm text-gray-700">{item.title}</li>
        ))}
      </ul>
      <h2 className="text-lg font-bold mb-2">월간 TOP5</h2>
      <ul className="space-y-1">
        {monthlyTop.map((item) => (
          <li key={item.id} className="text-sm text-gray-700">{item.title}</li>
        ))}
      </ul>
    </div>
  );
}
