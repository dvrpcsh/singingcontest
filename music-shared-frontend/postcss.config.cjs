// postcss.config.cjs
module.exports = {
  plugins: {
    '@tailwindcss/postcss': {},    // tailwindcss 대신 여기로
    autoprefixer: {},              // 기존 autoprefixer
  },
}
