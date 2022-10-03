apiclient = (function (){
    
    var getBlueprintsByNameAndAuthor = function(author,bpname,callback){
        var promise = $.ajax({
           type: "GET",
           url: "blueprints/"+author+"/"+bpname,
           contentType: "application/json; charset=utf-8",
           dataType: "json",
           
           success : function (data){
               callback(null,data);
           }
        });
    };
    
    var getBlueprintsByAuthor = function(author,callback){
        var promise = $.ajax({
           type: "GET",
           url: "blueprints/"+author,
           contentType: "application/json; charset=utf-8",
           dataType: "json",
           
           success : function (data){
               callback(null,data);
           }
        });
    };
    
    return{
        getBlueprintsByAuthor: getBlueprintsByAuthor,
        getBlueprintsByNameAndAuthor: getBlueprintsByNameAndAuthor
        
    };
})();


