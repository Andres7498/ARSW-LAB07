var elem = document.getElementById('myCanvas'),
    elemLeft = elem.offsetLeft + elem.clientLeft,
    elemTop = elem.offsetTop + elem.clientTop,
    context = elem.getContext('2d'),
    elements = [];
    const puntos = [];

// Add event listener for `click` events.
elem.addEventListener('click', function(event) {
    var x = event.pageX - elemLeft,
        y = event.pageY - elemTop;
    puntos.push(x);
    puntos.push(y);
    console.log(puntos);
}, false);


// Add element.
elements.push({
    colour: '#000000',
    width: 1000,
    height: 1000,
});

// Render elements.
elements.forEach(function(element) {
    context.fillStyle = element.colour;
    context.fillRect(element.left, element.top, element.width, element.height);
});