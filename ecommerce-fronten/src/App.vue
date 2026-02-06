<template>
  <div id="app">
    <Navbar />
    <RouterView />
    <footer class="bg-light text-center py-4 mt-5">
      <div class="container">
        <p class="text-muted mb-0">
          © 2026 E.SUN Bank E-Commerce System | 
          <a href="https://github.com/Jasper900807/esun-ecommerce" target="_blank" class="text-decoration-none">
            <i class="bi bi-github"></i> GitHub
          </a>
        </p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { RouterView } from 'vue-router';
import Navbar from './components/Navbar.vue';
</script>

<style>
/* 全域樣式 */
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

main {
  flex: 1;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.card {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1) !important;
}

.btn {
  transition: all 0.3s ease;
}

.btn:hover {
  transform: translateY(-2px);
}

.router-view {
  min-height: calc(100vh - 200px);
}
</style>