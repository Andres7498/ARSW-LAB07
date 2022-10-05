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

        })
    };
    var addBlueprint = function(points,author,bpname,callback){
           console.log(points);
           const put_request = $.ajax({
               url: "/blueprints/{author}/{bpname}/planos",
               type: "POST",
               data: '{"points":'+JSON.stringify(points)+',"bpname":'+bpname+',"author":'+author+'}',
               contentType: "application/json",
           }); callback(null,bpname,bpname);

    };
    var deleteBlueprints = function(author,blueprints,callback){
            var promise = $.ajax({
               type: "DELETE",
               url: "blueprints/"+author,
               contentType: "application/json; charset=utf-8",
               dataType: "json",

               success : function (data){
                   callback(null,data);
               }
            })
    };

    return{
        getBlueprintsByAuthor: getBlueprintsByAuthor,
        getBlueprintsByNameAndAuthor: getBlueprintsByNameAndAuthor,
        addBlueprint: addBlueprint,
        deleteBlueprints: deleteBlueprints
    };


})();


