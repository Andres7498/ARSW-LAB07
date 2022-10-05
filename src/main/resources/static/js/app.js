



/* global apiclient */

var modulo = (function () {
    
    /**
     * Consulta los blueprints de un autor y llama a la funcion privada para agregarlos a la tabla en la vista
     * @returns {undefined}
     */
    var getBlueprintsByAuthor = function () {
        author = $("#author").val();
        if (author === "") {
            alert("Nombre de Autor Vacio");
        } else {
            apiclient.getBlueprintsByAuthor(author, (req, resp) => {
                llenarTabla(resp);
            });
        }
    };
    /**
     * Consulta el blueprint por nombre y por autor y hace llamado a la funcion draw que pinta el blueprint en el canvas 
     * @param {type} data objeto que contiene los detalles de nombre y autor del blueprint seleccionado
     * @returns {undefined}
     */
    var getBlueprintsByNameAndAuthor = function(data){
        author = $("#author").val();
        blueprintName = data.id;
        apiclient.getBlueprintsByNameAndAuthor(author, blueprintName, (req, resp) => {
            draw(resp);
        });
    };
    
    /**
     * Pinta el blueprint en el canvas 
     * @param {type} data objeto que contiene toda la informacion del blueprint a pintar
     * @returns {undefined}
     */
    var draw = function (data){
        const puntos = data.points;
        var c = document.getElementById("myCanvas");
        var ctx = c.getContext("2d");
        ctx.clearRect(0, 0, c.width, c.height);
        ctx.restore();
        ctx.beginPath();
        for (let i = 1; i < puntos.length; i++) {
            ctx.moveTo(puntos[i - 1].x, puntos[i - 1].y);
            ctx.lineTo(puntos[i].x, puntos[i].y);
            if (i === puntos.length - 1) {
                ctx.moveTo(puntos[i].x, puntos[i].y);
                ctx.lineTo(puntos[0].x, puntos[0].y);
            }
        }
        ctx.stroke();
    };

    /**
     * Agrega los detalles de los blueprints en la tabla
     * @param {type} data objeto que contiene toda la informacion de los blueprints que se van a poner en la tabla
     * @returns {undefined}
     */
    var llenarTabla = function (data) {
        $("#tableBlueprints tbody").empty();
        $("#tableBlueprints tbody").append($("<tr><td> Author </td><td> Number Of Points </td><td> Open </td>"));

        if (data === undefined) {
            alert("Autor no encontrado");
            $("#author-name").empty();
            $("#user-points").empty();
        } else {
            const datanew = data.map((bp) => {
                return {
                    name: bp.name,
                    puntos: bp.points.length
                };
            });
            datanew.map((bps) => {
                $("#tableBlueprints > tbody:last").append($("<tr><td>" + bps.name + "</td><td>" + bps.puntos.toString() +
                        "</td><td>" + "<button  id=" + bps.name + " onclick=modulo.getBlueprintsByNameAndAuthor(this)>open</button>" + "</td>"));
            });
            const totalPuntos = datanew.reduce((suma, {puntos}) => suma + puntos, 0);
            $("#user-points").text(totalPuntos);
        }
        }
    var guardar = function () {
        author = document.getElementsByTagName('blueprint');
        if (author != "") {
            apiclient.addBlueprint (points,author,bpname,(req, resp) =>{
            });
        } else {
            console.log("El author no puede ser nulo");
        }
    };
    var deleteBlueprint = function (){
        author = $("#blueprint").val();
        if (author === "") {
            alert("Nombre de Autor Vacio");
        } else {
            apiclient.deleteBlueprint(author,bpname, (req, resp) => {
            });
        }
    };
    return {
        getBlueprintsByNameAndAuthor: getBlueprintsByNameAndAuthor,
        getBlueprintsByAuthor: getBlueprintsByAuthor,
        guardar: guardar,
        deleteBlueprint: deleteBlueprint
    };

})();