<template>
  <div class="home">
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
      <h3 class="header-title"><router-link to="/" class="logo" >小木科技</router-link></h3> 
      <el-menu-item index="4" class="nav-title nav-style" v-if="isLogin"><router-link to="/questionBank" class="nav-router">题库管理</router-link></el-menu-item>
      <el-menu-item index="3" class="nav-title nav-style" v-else><router-link to="/login" class="nav-router">登录</router-link></el-menu-item>
      <el-menu-item index="2" class="nav-title nav-style" ><router-link to="/contact" class="nav-router">联系我们</router-link></el-menu-item>
      <el-menu-item index="1" class="nav-title nav-style"><router-link to="/" class="nav-router">首页</router-link></el-menu-item>
    </el-menu>
    <div class="home-content">
      <el-carousel :height="heightnum" trigger="click" arrow="always">
        <el-carousel-item v-for="item in imgArr" :key="item" class="index-banner">
          <img :src="item" alt="" class="banner-img">
        </el-carousel-item>
      </el-carousel>
    </div>
    <el-button type="primary" class="apply-tel"><router-link to="/contact" class="nav-router-button" style="color: #fff!important">立即申请使用</router-link></el-button>
  </div>
</template>

<script>
export default {
  data () {
    return {
      heightnum: '100',
      activeIndex: '1',
      username: '',
      isLogin: false,
      imgArr: [
        '/static/img/dati1.png',
        '/static/img/dati2.png',
        '/static/img/dati3.png',
        '/static/img/dati4.png',
        '/static/img/dati5.png'
        
      ]
    }
  },
  created () {
    this.heightnum = (document.documentElement.clientHeight - 200).toString() + 'px'
    if (sessionStorage.getItem('username')) {
      this.isLogin = true
    } else {
      this.isLogin = false
    }
  }
}
</script>

<style>
.banner-img {
  height: 100%;
}
.home-content {
  width: 100%;
}
.header-title {
  margin: 0;
}
.apply-tel {
    position: absolute;
    bottom: 20px;
    right: 20px;
}
.nav-router {
  width: 100%;
  height: 100%;
  display: block;
  color: #fff;
}
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.index-banner {
  background: #fff
}
</style>
