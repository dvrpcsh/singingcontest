import React from "react";

// 샘플 비디오 데이터
const videos = [
  {
    id: 1,
    thumbnail: "https://i.ytimg.com/vi/n8kMLUghTVE/hq720.jpg?sqp=-oaymwEnCNAFEJQDSFryq4qpAxkIARUAAIhCGAHYAQHiAQoIGBACGAY4AUAB&amp;rs=AOn4CLBpFamqH-7IozliUFlzU1nEFC5l8Q",
    title: "이게 라이브라고..? 30명 동시에 소름이 돋는 역대급 LIVE 전건호 - 부디",
    views: "조회수 123만회",
    date: "7개월 전",
  },
  {
    id: 2,
    thumbnail: "https://i.ytimg.com/vi/PCpfFGA3D9E/hq720.jpg?sqp=-oaymwEnCNAFEJQDSFryq4qpAxkIARUAAIhCGAHYAQHiAQoIGBACGAY4AUAB&amp;rs=AOn4CLA6L5Ei6IRI5XilBdQlXWC75xJpEg",
    title: "[일소라] 듣는 사람 마음 후벼파는 '축가' (전우성(노을)) cover",
    views: "조회수 88만회",
    date: "5년전",
  },
  {
    id: 3,
    thumbnail: "https://i.ytimg.com/vi/84IVXVDqa5I/hq720.jpg?sqp=-oaymwEnCNAFEJQDSFryq4qpAxkIARUAAIhCGAHYAQHiAQoIGBACGAY4AUAB&amp;rs=AOn4CLAH-_8T-KWZUIxxeGpxlE4XyIokSw",
    title: "[일소라] 일반인 전태호 - 기다릴게 (하동균,이정) 레전드 부산 버스킹 cover",
    views: "조회수 592만회",
    date: "9년전",
  },
  {
    id: 4,
    thumbnail: "https://i.ytimg.com/vi/zprnY2Amldo/hq720.jpg?sqp=-oaymwEnCNAFEJQDSFryq4qpAxkIARUAAIhCGAHYAQHiAQoIGBACGAY4AUAB&amp;rs=AOn4CLCN-XC3euV8WRjM85rj5HWm-eRmYQ",
    title: "[일소라] 일반인이 버스킹하면서 부른 '그대가 나를 본다면' (반하나)",
    views: "조회수 103만회",
    date: "7년전",
  },
  {
    id: 5,
    thumbnail: "https://i.ytimg.com/vi/fVl48p9XxWw/hq720.jpg?sqp=-oaymwEnCNAFEJQDSFryq4qpAxkIARUAAIhCGAHYAQHiAQoIGBACGAY4AUAB&amp;rs=AOn4CLALP-Qn0Gr-ZVulgnLPxjDW4i0Nug",
    title: "[일소라] 일반인 노래실력 개쩌는 남자분의 '해줄 수 없는 일' (박효신/ 신용재ver) cover",
    views: "조회수 86만회",
    date: "7년전",
  },
];

function Home() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Singing Contest 🎤</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
        {videos.map((video) => (
          <div
            key={video.id}
            className="bg-white rounded-lg shadow-lg overflow-hidden"
          >
            {/* 이미지 컨테이너: 16:9 비율 유지 */}
            <div className="relative w-full aspect-w-16 aspect-h-9">
              <img
                src={video.thumbnail}
                alt={video.title}
                className="w-full h-full object-cover"
              />
            </div>
            {/* 텍스트 정보 */}
            <div className="p-4">
              <h3 className="text-lg font-bold">{video.title}</h3>
              <p className="text-sm text-gray-600">{video.views} • {video.date}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Home;
