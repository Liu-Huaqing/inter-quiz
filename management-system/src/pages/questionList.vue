<template>
  <div class="question-list">
    <el-row class="tac">
      <el-col :span="3">
        <h3><router-link to="/">小木科技</router-link></h3>
        <el-menu
          default-active="1"
          class="el-menu-vertical">
          <el-menu-item index="1" @click="routerUrl('/questionBank')">
            <i class="el-icon-menu"></i>
            <span slot="title">题库管理</span>
          </el-menu-item>
          <el-menu-item index="2" @click="routerUrl('/setting')">
            <i class="el-icon-setting"></i>
            <span slot="title">活动管理</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="21" class="question-list-content">
        <el-row>
          <el-col :span="4"  class="que-content-title"><h3>修改题库</h3> </el-col>
          <el-col :span="3" :offset="17" class="user">{{username}}</el-col>
        </el-row>
        <el-row class="create-que-bank">
          <el-col :span="6" :offset="18">
            <el-button plain @click="goreturn()">返回</el-button>
            </el-col>
        </el-row>
        <div class="quest-content-list">
          <div style="text-align: left">{{questionName}}</div>
          <el-card class="box-card" v-for="(item, index) in questions" :key="item">
            <div slot="header" class="clearfix">
              <span class="ques-card-title">问题：{{ item.title }} <br> 正确选项：{{item.resultIndex === 0 ? '选项1' : (item.resultIndex === 1 ? '选项2' : '选项3')}}</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="removeArr(index)">删除</el-button>&nbsp;
              <el-button style="float: right; padding: 3px 0" type="text" @click="editArr(index)">编辑</el-button>
            </div>
            <div class="text item">
              选项1：{{ item.options[0]}}
            </div>
            <div class="text item">
              选项2：{{ item.options[1]}}
            </div>
            <div class="text item">
              选项3：{{ item.options[2]}}
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>
     <el-dialog title="提示" :visible.sync="dialogVisible" width="30%">
      <span>{{message}}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDelog()">取 消</el-button>
        <el-button type="primary" @click="closeDelog(dialogPamas)">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import utils from '@/assets/utils'
export default {
  data () {
    return {
      username: '',
      dialogVisible: false,
      message: '抱歉，获取题目列表失败，是否重试？',
      dialogPamas: 1,
      questionName: '',
      questions: [
        {
          title: '屈原姓什么？',
          options: ['屈', '张', '芈'],
          resultIndex:  [0]
        }, {
          title: '屈原姓什么？',
          options: ['屈', '张', '芈'],
          resultIndex:  [0]
        }, {
          title: '屈原姓什么？',
          options: ['屈', '张', '芈'],
          resultIndex:  [0]
        }
      ]
    }
  },
  created () {
    if (sessionStorage.getItem('username')) {
      this.username = sessionStorage.getItem('username')
      this.getquebank()
    } else {
      this.$router.push({path:'/login'})
    }
  },
  methods: {
    routerUrl (url) {
      this.$router.push({path: url})
    },
    goreturn () {
      this.$router.go(-1)
    },
    getquebank () {
      var frag = utils.getFrag()
      this.$ajax({
        method: 'get',
        url: '/question_bank/' + frag.ques,
      }).then(resp => {
        this.dialogVisible = false
        this.questions = resp.questions
        this.questionName = resp.name
      }).catch(e => {
        this.message = '抱歉，获取题目列表失败，是否重试？'
        this.dialogPamas = 1
        this.dialogVisible = true
      })
    },
    closeDelog (index) {
      this.dialogVisible = false
      var self = this
      if (index === 1) {
        setTimeout(() => {
          this.getquebank()
        }, 1000)
      } else if (index === 2) {
        this.questions.splice(index, 1)
        self.$ajax({
          method: 'put',
          url: '/question_bank/' + utils.getFrag().ques,
          data: {
            name: self.questionName,
            questions: self.questions
          }
        }).then(resp => {
          this.$message({
            message: '恭喜您，成功修改本题库',
            type: 'success'
          })
          setTimeout(() => {
            this.getquebank()
          }, 1000)
        }).catch(e => {
          this.$message.error('抱歉，修改题库不成功，请重试')
          self.isLoading = false
          setTimeout(() => {
            this.getquebank()
          }, 1000)
        })
      }
    },
    editArr (index) {
      this.$router.push({path:'/questionBank/createBank', query:{ques: utils.getFrag().ques, index: index}})
    },
    removeArr (index) {
      this.message = '您确定删除该题目么？'
      this.dialogPamas = 2
      this.dialogVisible = true
    }
  }
}
</script>

<style>
.tac >.el-col-3 {
  border-right: solid 1px #e6e6e6;
  height: 100vh;
}
.tac >.el-col-3 > ul {
  border: none;
}
.que-content-title {
  text-align: left;
}
.que-content-title h3 {
  margin: 0;
}
.question-list-content {
  padding: 20px;
  height: 100vh;
  overflow: auto;
}
.create-que-bank {
  text-align: right;
  margin-top: 10px;
}
.user {
  text-align: right;
}
.box-card {
  width: 30%;
  text-align: left;
  display: inline-block;
  margin: 10px;
}
.text {
  font-size: 14px;
}
.item {
  margin-bottom: 18px;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
.quest-content-list {
  text-align: left;
}
.ques-card-title {
  display: inline-block;
  width: 70%
}
</style>
