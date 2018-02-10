<template>
  <div class="question-bank">
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
      <el-col :span="21" class="question-content">
        <el-row>
          <el-col :span="2" class="que-content-title"><h3>题库管理</h3></el-col>
          <el-col :span="3" :offset="19" class="user">{{username}}</el-col>
        </el-row>
        <el-row class="create-que-bank">
          <el-col :span="3" :offset="21">
            <el-button type="primary" @click="handleEdit('cre')">创建题库</el-button>
            </el-col>
        </el-row>
        <div class="table-list">
          <el-table
            :data="questionBanks"
            height="580"
            style="width: 100%">
            <el-table-column
              prop="id"
              label="id"
              width="180">
            </el-table-column>
            <el-table-column
              prop="name"
              label="题库名称"
              width="180">
            </el-table-column>
            <el-table-column
              prop="createdAt"
              label="创建时间">
            </el-table-column>
            <el-table-column
              prop="updatedAt"
              label="更新时间">
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleEdit(scope.row.id)">编辑</el-button>
                <el-button
                  size="mini"
                  type="danger"
                  @click="handleDelete(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
    <el-dialog
    title="提示" :visible.sync="dialogVisible" width="30%">
      <span>{{message}}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDelog()">取 消</el-button>
        <el-button type="primary" @click="closeDelog(closeDelogParams)">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        username: '',
        closeDelogParams: '',
        message: '获取列表失败，请重试呢',
        dialogVisible: false,
        questionBanks: [{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "3",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
        },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "3",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "3",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "3",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "3",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "3",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "3",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          },{
          id: "1",
          name: "test",
          createdAt: 151739824367,
          updatedAt: 1517398243671
          }]
      }
    },
    created () {
      if (sessionStorage.getItem('username')) {
        this.username = sessionStorage.getItem('username')
        this.getquebank()
      } else {
        this.$router.push({path:'/login'})
      }
      //伪代码
      for (var i = 0; i < this.questionBanks.length; i++) {
        this.questionBanks[i].createdAt = this.timestampToDate(this.questionBanks[i].createdAt)
        this.questionBanks[i].updatedAt = this.timestampToDate(this.questionBanks[i].updatedAt)
      }
    },
    methods: {
      getquebank () {
        var self = this
        self.$ajax({
          method: 'get',
          url: '/question_bank/list',
          data: {name: self.username}
        }).then(res => {
          if (res.questionBanks.length > 0) {
            for (var i = 0; i < res.questionBanks.length; i++) {
              res.questionBanks[i].createdAt = this.timestampToDate(res.questionBanks[i].createdAt)
              res.questionBanks[i].updatedAt = this.timestampToDate(res.questionBanks[i].updatedAt)
            }

            self.questionBanks = res.questionBanks
          }
        }).catch(res => {
          this.message = '获取列表失败，请重试呢'
          this.closeDelogParams = 'list'
          this.dialogVisible = true
        })
      },
      routerUrl (url) {
        this.$router.push({path: url})
      },
      closeDelog(done) {
        var self = this
        if (!done) {
          self.dialogVisible = false
        } else if (done === 'list') {
          self.dialogVisible = false
          setTimeout(() => {
            self.getquebank()
          }, 1000);
        } else {
          self.$ajax({
            method: 'delete',
            url: '/question_bank',
            data: {id: done}
          }).then(res => {
            this.dialogVisible = false
            this.$message({
              message: '您已成功删除该题库',
              type: 'success'
            });
            setTimeout(() => {
              self.getquebank()
            }, 1000);
          }).catch(res => {
            this.dialogVisible = false
            this.$message({
              message: '抱歉，删除题库失败，请重试',
              type: 'error'
            });
            setTimeout(() => {
              self.getquebank()
            }, 1000);
          })

        }
        
      },
      handleEdit (index) {
        if (index === 'cre') {
          this.$router.push({path:'/questionBank/createBank'})
        } else {
          this.$router.push({path:'/questionBank/quest/list', query: {ques: index}})
        }
        
      },
      handleDelete (index) {
        this.closeDelogParams = index
        this.message = '您确定删除此题库么？'
        this.dialogVisible = true
      },
      timestampToDate (timestamp) {
        var dataTime = new Date(parseInt(timestamp)).toLocaleString();
        var dataArr = dataTime.split('/');
        if (dataArr[1].length == 1) {
          dataArr[1] = '0' + dataArr[1]
        }
        if (dataArr[2].replace(/^\s*|\s*$/g, '').length == 1) {
          dataArr[2] = '0' + dataArr[2]
        }
        return dataArr[0] + '-' + dataArr[1] + '-' + dataArr[2].replace(/^\s*|\s*$/g, '');
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
.question-content {
  padding: 20px;
}

.table-list {
  margin-top: 30px;
  text-align: center
}

.table-list .cell {
  text-align: center;
}
.create-que-bank {
  margin-top: 20px;
  text-align: right;
}
.que-content-title {
  text-align: left;
}
.que-content-title h3 {
  margin: 0;
}
.user {
  text-align: right;
}

</style>

