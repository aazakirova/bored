require: slotfilling/slotFilling.sc
  module = sys.zb-common
  
require: localPatterns.sc

require: function.js

init:
    bind("postProcess", function($context) {
        $context.session.lastState = $context.currentState;
    })
    
theme: /

    state: Welcome
        q!: *start
        q!: $hi
        random:
            a: Hello! 
            a: Hi! 
        a: I am {{$injector.botName}}. I am always happy to find new activities for you :)
        script:
            $jsapi.startSession()
        go!: /howAreYou
        

            
    state: CatchAll   
        event!: noMatch
        random:
            a: Sorry, I don't understand you. Put your question another way, please. 
            a: Unfortunately, I don't have an answer for you. Write somethimg else, please. 
            a: I think, I don't get it. Say it in other words, please. 
        go!: {{$session.lastState}}    
            
    state: howAreYou 
        random:
            a: How is your day going?
            a: How are you?
        
        state: GoodMood
            q: * (good* / well / ok* / cool / nice / fine / happy* / fifty fifty / so so) *
            a: I can make your day even cooler!
            go!: /NormalButtons
            
        state: BadSad
            q: * (bad* / sad* / tough* / rough*) *
            a: I can try to make your day a little bit nicer!
            go!: /NormalButtons
            
    state: NormalButtons
        buttons:
            "Let's get started!" -> /activityType
            "Cancel" -> /Welcome
            
    state: activityType
        a: Choose which type of activity you would like to do: Education, Recreational, Social, DIY, Charity, Cooking, Relaxation, Music, Busywork.
        q: (* Education / Recreational / Social / DIY / Charity / Cooking / Relaxation / Music / Busywork *)
        script:
            log(toPrettyString($parseTree));
            $session.type = $parseTree._Root;
        go!: /Play
        
    state: Play
        script: 
            $temp.task = getActivity($session.type);
        if: $temp.activity
            a: That is my idea: {{$temp.task.activity}}. I hope you like it!
        
        
        
        
