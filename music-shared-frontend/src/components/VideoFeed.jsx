// src/components/VideoFeed.jsx
import React from 'react';
import VideoCard from './VideoCard';

const mockVideos = [
  { id: 1, title: '커버곡 1', thumbnail: 'https://dummyimage.com/320x180/000/fff'},
  { id: 2, title: '자작곡 1', thumbnail: 'https://dummyimage.com/320x180/000/fff'},
  // 더미 데이터 추가
];

export default function VideoFeed() {
  return (
    <div className="grid grid-cols-3 gap-4">
      {mockVideos.map((video) => (
        <VideoCard key={video.id} video={video} />
      ))}
    </div>
  );
}
