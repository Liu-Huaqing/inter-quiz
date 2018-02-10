<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style>
body {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #4a4c5b;
  font-size: 14px;
  box-sizing: border-box;
}
#app .nav-title {
  line-height: 44px;
  height: 44px;
  font-size: 18px;
}
#app .nav-style {
  float: right;
  font-size: 14px;
  padding: 0;
}
#app a {
  text-decoration: none;
}
a:focus {
  outline:none;
}
.logo {
  float: left;
  line-height: 44px;
  font-size: 18px;
  color: #333;
  margin-left: 20px;
}
#app .nav-router {
  width: 100%;
  height: 100%;
  display: block;
  text-align: center;
  padding: 0 20px;
  box-sizing: border-box;
}

.apply-tel {
    position: absolute;
    bottom: 20px;
    right: 20px;
    padding: 0;
}
.nav-router-button {
  display: block;
  text-align: center;
  padding: 12px 20px;
  box-sizing: border-box;
  color: #fff;
}

a {
  color: #4a4c5b!important;
}
</style>
