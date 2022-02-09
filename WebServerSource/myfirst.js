const fs = require('fs')
const bodyParser = require('body-parser')//install
const express = require('express');//install

const app = express();

var canAdd="true";

app.use(bodyParser.urlencoded({ extended: false }))
 
app.get('/', (req, res) => {
  res.writeHead(200, { 'Content-Type': 'application/json' });
   fs.readFile('example.txt',null,function(error,data){
      if(error){
        throw error;
      }else{
        res.write(data.toString());
        res.end();
      }
   })
});
 
app.post('/', (req, res) => {
 const dataReq={
   star:req.body.star,
   title:req.body.title
 }

 fs.readFile('example.txt',null,function(error,data){//read the file to change the rating
  if(error){
    throw error;
  }else{
    //compute the string to json and back 
    var cont="["
    JSON.parse(data).forEach(element => {
      if(element.title.toString()==dataReq.title){
        const el=element;

        if(canAdd=="true"){//because it enters 2 times
        el.stars=el.stars+dataReq.star;
        canAdd="false"
        console.log(dataReq.title+" was rated with "+dataReq.star+"!")//Rate
        }else{
          canAdd="true"
        }
        cont=cont+JSON.stringify(el)+",\n";
       
      }else{
        cont=cont+JSON.stringify(element)+",\n";
      }
    });
    
    cont=cont.substring(0,cont.length-2)
    cont=cont+"]";

    fs.writeFile('example.txt', cont, function (err) {//write the update to file
      if (err) return console.log(err);
      
    });
    

  }
})

});
 
app.listen(8080, () => {
  console.log('Our express server is up on port 8080');
});






