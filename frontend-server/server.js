var express = require('express');
var app = express();

// all environments
app.set('port', 8000);
app.use(express.static("."))

app.listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});
