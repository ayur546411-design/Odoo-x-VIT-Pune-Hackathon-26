/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      fontFamily: {
        display: ["'Inter'", "system-ui", "sans-serif"],
        body: ["'Inter'", "system-ui", "sans-serif"],
      },
      colors: {
        primary: {
          50: "#f0eeff",
          100: "#e0dcff",
          200: "#c4baff",
          300: "#a18eff",
          400: "#7B61FF",
          500: "#6347e0",
          600: "#4E3AB1",
          700: "#3d2e8c",
          800: "#2B2A4C",
          900: "#1e1b3a",
          950: "#141225",
        },
        accent: {
          50: "#f0fdf4",
          100: "#dcfce7",
          200: "#bbf7d0",
          300: "#86efac",
          400: "#4ade80",
          500: "#22c55e",
          600: "#16a34a",
        },
        surface: {
          50: "#ffffff",
          100: "#F7F8FC",
          200: "#EEF0F6",
          300: "#E2E5EF",
          400: "#C8CBD8",
        },
        navy: {
          50: "#f0f1f5",
          100: "#d1d5e0",
          200: "#9da4b8",
          300: "#6B7280",
          400: "#4B5563",
          500: "#374151",
          600: "#1F2937",
          700: "#1A1A2E",
          800: "#141425",
          900: "#0f0f1e",
        },
      },
      borderRadius: {
        "4xl": "2rem",
        "5xl": "2.5rem",
      },
      animation: {
        "fade-up": "fadeUp 0.7s ease forwards",
        "fade-in": "fadeIn 0.5s ease forwards",
        "slide-right": "slideRight 0.6s ease forwards",
        "slide-left": "slideLeft 0.6s ease forwards",
        float: "float 6s ease-in-out infinite",
        "float-slow": "float 8s ease-in-out infinite",
        "pulse-soft": "pulseSoft 3s ease-in-out infinite",
        "scale-in": "scaleIn 0.5s ease forwards",
        marquee: "marquee 30s linear infinite",
      },
      keyframes: {
        fadeUp: {
          "0%": { opacity: 0, transform: "translateY(30px)" },
          "100%": { opacity: 1, transform: "translateY(0)" },
        },
        fadeIn: {
          "0%": { opacity: 0 },
          "100%": { opacity: 1 },
        },
        slideRight: {
          "0%": { opacity: 0, transform: "translateX(-40px)" },
          "100%": { opacity: 1, transform: "translateX(0)" },
        },
        slideLeft: {
          "0%": { opacity: 0, transform: "translateX(40px)" },
          "100%": { opacity: 1, transform: "translateX(0)" },
        },
        float: {
          "0%, 100%": { transform: "translateY(0px)" },
          "50%": { transform: "translateY(-14px)" },
        },
        pulseSoft: {
          "0%, 100%": { opacity: 0.6 },
          "50%": { opacity: 1 },
        },
        scaleIn: {
          "0%": { opacity: 0, transform: "scale(0.92)" },
          "100%": { opacity: 1, transform: "scale(1)" },
        },
        marquee: {
          "0%": { transform: "translateX(0)" },
          "100%": { transform: "translateX(-50%)" },
        },
      },
      boxShadow: {
        glass: "0 8px 32px rgba(0, 0, 0, 0.06)",
        card: "0 2px 16px rgba(0, 0, 0, 0.06)",
        "card-hover": "0 8px 40px rgba(123, 97, 255, 0.12)",
        elevated: "0 20px 60px rgba(0, 0, 0, 0.1)",
        glow: "0 0 40px rgba(123, 97, 255, 0.2)",
      },
    },
  },
  plugins: [],
};