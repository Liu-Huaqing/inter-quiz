<template>
  <div>
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
      <h3 class="header-title"><router-link to="/" class="logo">小木科技</router-link></h3> 
      <el-menu-item index="3" class="nav-title nav-style"><router-link to="/login" class="nav-router">登录</router-link></el-menu-item>
      <el-menu-item index="2" class="nav-title nav-style" ><router-link to="/contact" class="nav-router">联系我们</router-link></el-menu-item>
      <el-menu-item index="1" class="nav-title nav-style"><router-link to="/" class="nav-router">首页</router-link></el-menu-item>
    </el-menu>
    <div class="login-form">

    <el-form :model="ruleForm2" status-icon :rules="rules2" ref="ruleForm2" label-width="100px" class="demo-ruleForm">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="ruleForm2.username"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="ruleForm2.password" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm2')" :loading="isLoading">提交</el-button>
        <el-button @click="resetForm('ruleForm2')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
  <el-dialog
  title="提示"
  :visible.sync="dialogVisible"
  width="30%"
  :before-close="handleClose">
    <span>登录失败，请重试呢</span>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="closeDelog(1)">确 定</el-button>
    </span>
  </el-dialog>
  </div>
  
</template>

<script>
export default {
  data () {
    var checkUser = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("账号不能为空"))
      } else {
        callback();
      }
    }
    var validatePassword = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"))
      } else {
        callback();
      }
    }
    return {
      activeIndex: '3',
      isLoading: false,
      dialogVisible: false,
      ruleForm2: {
        password: "",
        username: "",
      },
      rules2: {
        password: [{ validator: validatePassword, trigger: "blur" }],
        username: [{ validator: checkUser, trigger: "blur" }]
      }
    }
  },
  methods: {
    submitForm (formName) {
      //weidaima
      // sessionStorage.setItem('username','zhouwenkai');
      // this.$router.push({path:'/questionBank'})
      //fengexian
      var self = this
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.isLoading = true
          self.$ajax({
            method: 'post',
            url: '/login',
            data: self.ruleForm2
          }).then(res => {
            sessionStorage.setItem('username',self.ruleForm2.username);
            this.$router.push({path:'/questionBank'})
          }).catch(function (e) {
            self.dialogVisible = true
          })
        } else {
          return false
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    },
    closeDelog(done) {
      this.dialogVisible = false
      this.isLoading = false
    }
  }
}
</script>

<style>
.login-form {
  margin: 100px 300px;
  border: 1px solid #ebebeb;
  padding: 50px 100px;
  -webkit-box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
}
.header-title {
  margin: 0;
}
</style>
