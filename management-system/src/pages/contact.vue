<template>
  <div class="contact">
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
      <h3 class="header-title"><router-link to="/" class="logo">小木科技</router-link></h3> 
      <el-menu-item index="4" class="nav-title nav-style" v-if="isLogin"><router-link to="/questionBank" class="nav-router">题库管理</router-link></el-menu-item>
      <el-menu-item index="3" class="nav-title nav-style" v-else><router-link to="/login" class="nav-router">登录</router-link></el-menu-item>
      <el-menu-item index="2" class="nav-title nav-style" ><router-link to="/contact" class="nav-router">联系我们</router-link></el-menu-item>
      <el-menu-item index="1" class="nav-title nav-style"><router-link to="/" class="nav-router">首页</router-link></el-menu-item>
    </el-menu>
    <div class="contact-form">
      <h4>申请免费使用</h4>
      <div>
        <el-form :inline="true" :model="formInline" class="demo-form-inline">
          <el-form-item label="手机号：">
            <el-input v-model="phoneNumber" placeholder="请输入您的手机号"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit" >提交</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="contact-us">
      <h4>联系我们</h4>
      <p>
        服务电话：{{contact_us.tel}}&#X3000;&#X3000;&#X3000;&#X3000;微信号：{{contact_us.wx}}
      </p>
    </div>
    <div class="contact-about">
      <h4>关于我们</h4>
      <p>我们是一家非常nice的公司，相信我们，给你一个surprise</p>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      activeIndex: '2',
      phoneNumber: '',
      isLogin: false,
      contact_us: {
        tel: '17610826989',
        wx: '17610826989'
      }
    }
  },
  created () {
    if (sessionStorage.getItem('username')) {
      this.isLogin = true
    } else {
      this.isLogin = false
    }
  },
  methods: {
    onSubmit () {
      var self = this
      self.$ajax({
        method: 'post',
        url: '/apply_for_use',
        data: {phoneNumber: self.phoneNumber}
      }).then(resp => {
        this.$message({
          message: '感谢您对我们的信任，我们将尽快联系您',
          type: 'success'
        })
      }).catch(e => {
        this.$message({
          message: '抱歉，提交没成功，请重试',
          type: 'error'
        })
      })
      
    }
  }
}
</script>

<style>
h4 {
  font-weight: 400;
  margin: 40px;
  font-size: 16px;
}
.header-title {
  margin: 0;
}

section {
  display: inline-block;
}

.contact-about {
  padding: 0 300px;
  border-bottom: 1px solid #ebebeb;
  min-height: 30vh;
}

.contact-about p {
  text-align: justify;
}

.contact-us {
  height: 30vh;
  border-bottom: 1px solid #ebebeb;
}

.contact-form {
  height: 30vh;
  border-bottom: 1px solid #ebebeb;
}
</style>
