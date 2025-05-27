// vite.config.js
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import tailwindcss from "tailwindcss";
import autoprefixer from "autoprefixer";

export default defineConfig({
  plugins: [react()],
  css: {
    postcss: {
      plugins: [
        tailwindcss(),
        autoprefixer(),
      ],
    },
  },
  server: {
    proxy: {
        '/api': {
            target: 'http://localhost:8080', //Spring Boot 서버 URL
            changeOrigin: true,
            secure: false,
            rewrite: (path) => path.replace(/^\/api/, '') // '/api' 경로 제거

        }
    }
  }
});
