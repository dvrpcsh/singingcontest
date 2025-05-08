// src/components/VideoCard.jsx
import React, { useRef } from 'react';

export default function VideoCard({ video }) {
  const videoRef = useRef(null);
  return (
    <div className="relative group">
      <video
        ref={videoRef}
        src={video.highlightUrl}
        poster={video.thumbnail}
        className="w-full h-auto rounded"
        muted
        onMouseEnter={() => videoRef.current?.play()}
        onMouseLeave={() => {
          if (videoRef.current) {
            videoRef.current.pause();
            videoRef.current.currentTime = 0;
          }
        }}
      />
      <div className="mt-2 text-sm font-medium text-gray-800">{video.title}</div>
    </div>
  );
}
