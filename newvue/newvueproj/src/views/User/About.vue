<template>
  <div class="card">
    <div class="about">
      <h1>关于我们</h1>
      <p>欢迎来到我们的应用！我们致力于提供最佳的用户体验。</p>
      <h2>我们的使命</h2>
      <p>我们旨在通过创新和技术来改善人们的生活。</p>
      <h2>联系方式</h2>
      <p>如需了解更多信息，请联系我们：</p>
      <ul>
        <li>Email: contact@example.com</li>
        <li>电话: 123-456-7890</li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: 'About'
}
</script>

<style scoped>
.about {
  max-width: 600px;
  margin: auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

h1 {
  color: #333;
}

h2 {
  color: #555;
}

p {
  line-height: 1.6;
}
</style>