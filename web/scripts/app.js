form = $("#log-in-form")
game = $("#game")

form.on('submit', function(e){
    e.preventDefault();
    $("#wrong-text").slideUp();
    $("#no-text").slideUp();
    $("#loading-gif").slideDown();
    brugernavn = form.find('input[name=brugernavn]').val();
    password = form.find('input[name=password]').val();
    // console.log("Brugeren vil logge ind med "+brugernavn+", "+password);
    // console.log({"brugernavn":brugernavn, "password": password});

    $.ajax({
        url: "rest/login",
        method: "POST",
        contentType: 'application/json',
        data:  JSON.stringify(form.serializeJSON()),
        success: function(data, textStatus, jqXhr){
            localStorage.setItem("jwt",jqXhr.getResponseHeader('Authorization').substring(7));
            form.slideUp();
            game.slideDown();
            startGame();

        },
        error: function(data){
            $("#wrong-text").slideDown();
            $("#no-text").slideUp();
        }
    });
    $("#loading-gif").slideUp();
});

$("#logout-button").on('click', function(){
    localStorage.removeItem("jwt");
    game.slideUp();
    form.slideDown();
});

function startGame(){
    keyboard.find("div a").each(function(i, a){
        $(a).css('visibility', 'visible');
    });
    usedkeyboard.find("div a").each(function(i, a){
        $(a).css('visibility', 'hidden');
    });
    $.ajax({
        url: "rest/hangman/state",
        method: "GET",
        success: function(data){
            updateGame(data)
        },
        error: function(data){
            console.log("Kunne ikke hente ordet");
        },
        beforeSend: function(xhr, settings) {
            xhr.setRequestHeader('Authorization','Bearer ' + localStorage.getItem("jwt"));
        }
    })
}

function startHighscore(){
    $.ajax({
        url: "rest/highscore/all",
        method: "GET",
        success: function(data){
            updateHighscore(data)
        },
        error: function(data){
            console.log("Kunne ikke hente highscore");
        },
        beforeSend: function(xhr, settings) {
            xhr.setRequestHeader('Authorization','Bearer ' + localStorage.getItem("jwt"));
        }
    });
}


function startMyscore(){
    $.ajax({
        url: "rest/highscore/me",
        method: "GET",
        success: function(data){
            updateMyscore(data)
        },
        error: function(data){
            console.log("Kunne ikke hente dine ord");
        },
        beforeSend: function(xhr, settings) {
            xhr.setRequestHeader('Authorization','Bearer ' + localStorage.getItem("jwt"));
        }
    });
}

function restartGame(){
    keyboard.find("div a").each(function(i, a){
        $(a).css('visibility', 'visible');
    });
    usedkeyboard.find("div a").each(function(i, a){
        $(a).css('visibility', 'hidden');
    });

    $.ajax({
        url: "rest/hangman/restart",
        method: "POST",
        success: function(data){
            updateGame(data)
            $('#lostgame').foundation('close');
            lostloading.hide();
            wonloading.hide();
        },
        error: function(data){
            console.log("Kunne ikke genstarte spil");
        },
        beforeSend: function(xhr, settings) {
            xhr.setRequestHeader('Authorization','Bearer ' + localStorage.getItem("jwt"));
        }
    })
}

word = $("#word");
wrongs = $("#wrongs")
grafik = $("#grafik")

function updateGame(gamestate){
    wrongs.text(gamestate.mistakes);
    grafik.attr("src","grafik/galge_"+gamestate.mistakes+".png");
    word.text(gamestate.word);
    for (var i in gamestate.bogstaver) {
        keyboard.find("div a:contains("+gamestate.bogstaver[i]+")").each(function(i, a){
            $(a).css('visibility', 'hidden');
        });
        usedkeyboard.find("div a:contains("+gamestate.bogstaver[i]+")").each(function(i, a){
            $(a).css('visibility', 'visible');
        });
    }

    if (gamestate.winstate === 1){
        // Spillet er vundet
        $("#wonword").text(gamestate.word);
        $("#wonmistakes").text(gamestate.mistakes);
        $('#wongame').foundation('open');
    } else if (gamestate.winstate ===-1){
        // Spillet er tabt
        $("#lostword").text(gamestate.word);
        $('#lostgame').foundation('open');
    }
}

highscoretbl = $("#highscoretabel")
function updateHighscore(highscorestate){
    highscoretbl.empty();
    keys = Object.keys(highscorestate)
    for(var i=0;i<keys.length;i++){
        brugernavn = keys[i];
        point = highscorestate[brugernavn];

        highscoretbl.prepend("<tr><td>"+brugernavn+"</td><td>"+point+"</td></tr>")
    }
}

function updateMyscore(myscorestate){
    if ('guessedWords' in myscorestate){
        $("#myscore-cloud").show();
        $("#myscore-text").hide();
        cloudlist = [];

        for (var i = 0; i < myscorestate.guessedWords.length; i++) {
            cloudlist.push([myscorestate.guessedWords[i], Math.floor(Math.random() * (50 - 25 + 1) + 50)]);
        }
        WordCloud(document.getElementById('myscore-cloud'), {list: cloudlist});

    } else {
        $("#myscore-cloud").hide();
        $("#myscore-text").show();
    }
}



// Spil nav
spilknap = $("#game-button");
scoreknap = $("#highscore-button");
myscoreknap = $("#myscore-button")
spilsted = $("#game-place");
scorested = $("#highscore-place");
myscorested = $("#myscore-place");

spilknap.on('click', function(){
    spilknap.removeClass("secondary");
    scoreknap.addClass("secondary");
    myscoreknap.addClass("secondary");

    spilsted.slideDown();
    scorested.slideUp();
    myscorested.slideUp();

    startGame();
});

scoreknap.on('click', function(){
    spilknap.addClass("secondary");
    scoreknap.removeClass("secondary");
    myscoreknap.addClass("secondary");

    spilsted.slideUp();
    scorested.slideDown();
    myscorested.slideUp();

    startHighscore();
});

myscoreknap.on('click', function(){
    spilknap.addClass("secondary");
    scoreknap.addClass("secondary");
    myscoreknap.removeClass("secondary");

    spilsted.slideUp();
    scorested.slideUp();
    myscorested.slideDown();

    startMyscore();
});


keyboard = $("#keyboard")
usedkeyboard = $("#used-keyboard")
keyboard.on('click', 'a', function () {
    bogstav = $(this).text();
    // console.log(bogstav);
    $(this).css('visibility', 'hidden');
    usedkeyboard.find("div a:contains("+bogstav+")").each(function(i, a){
        $(a).css('visibility', 'visible');
    });
    //
    // $(this).prop( "disabled", true );

    gaet(bogstav)
})


function gaet(bogstav){
    $.ajax({
        url: "rest/hangman/gaet",
        method: "POST",
        contentType: 'application/json',
        data: bogstav,
        success: function(data){
            updateGame(data)
        },
        error: function(data){
            console.log(data)
        },
        beforeSend: function(xhr, settings) {
            xhr.setRequestHeader('Authorization','Bearer ' + localStorage.getItem("jwt"));
        }
    })
}


wonbutton = $("#wonbutton")
lostbutton = $("#lostbutton")
wonloading = $("#wonloading")
lostloading = $("#lostloading")

wonbutton.on('click', function(){
    wonloading.slideDown();
    restartGame();
});

lostbutton.on('click', function(){
    lostloading.slideDown();
    restartGame();
});


if (localStorage.getItem("jwt")){
    form.hide();
    startGame();
    game.show();
}