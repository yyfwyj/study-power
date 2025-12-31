<template>
  <div class="home-page">
    <div class="hero-section">
      <h1 class="title">Vue3 弹窗组件示例</h1>
      <p class="subtitle">体验现代化的弹窗交互效果</p>
      <button class="open-modal-btn" @click="openModal">
        打开弹窗
      </button>
    </div>

    <div class="features-section">
      <div class="feature-card">
        <h3>🎨 美观设计</h3>
        <p>现代化的UI设计，支持多种主题</p>
      </div>
      <div class="feature-card">
        <h3>⚡ 高性能</h3>
        <p>基于Vue3 Composition API，性能优化</p>
      </div>
      <div class="feature-card">
        <h3>♿ 无障碍</h3>
        <p>完整支持键盘导航和屏幕阅读器</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { showModal, showConfirm, showAlert } from './useModal.mjs'

const openModal = () => {
  showModal({
    title: '欢迎使用命令式弹窗',
    content: '这是一个通过函数调用打开的弹窗，无需在模板中定义组件！',
    showFooter: true,
    onConfirm: () => {
      console.log('用户点击了确认')
      showAlert({
        title: '成功',
        message: '操作已确认！'
      })
    },
    onCancel: () => {
      console.log('用户点击了取消')
    }
  })
}

const openConfirm = () => {
  showConfirm({
    title: '删除确认',
    message: '确定要删除这条记录吗？此操作不可恢复。',
    onConfirm: () => {
      showAlert({
        title: '已删除',
        message: '记录已成功删除'
      })
    }
  })
}

const openAlert = () => {
  showAlert({
    title: '提示',
    message: '这是一个简单的提示信息'
  })
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.hero-section {
  text-align: center;
  padding: 80px 20px;
  color: white;
}

.title {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 16px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.subtitle {
  font-size: 1.2rem;
  margin-bottom: 40px;
  opacity: 0.9;
}

.open-modal-btn {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  border: 2px solid rgba(255, 255, 255, 0.3);
  color: white;
  padding: 16px 32px;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.open-modal-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.2);
}

.open-modal-btn:active {
  transform: translateY(0);
}

.features-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  max-width: 1000px;
  margin: 0 auto;
  padding: 40px 20px;
}

.feature-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  padding: 24px;
  color: white;
  text-align: center;
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-4px);
}

.feature-card h3 {
  font-size: 1.5rem;
  margin-bottom: 12px;
}

.feature-card p {
  opacity: 0.8;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .title {
    font-size: 2rem;
  }

  .hero-section {
    padding: 60px 20px;
  }

  .features-section {
    grid-template-columns: 1fr;
  }
}
</style>
