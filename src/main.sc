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
        go!: /Offer
    
    state: Offer 
        a: Great chose! Let's see what {{$session.type}} activity I can offer you.
        go!: /Play
        
    state: Play
        script: 
            $temp.task = getActivity($session.type);
        if: $temp.task
            random:
                a: Look what idea I've found for you! {{$temp.task.activity}}. I hope you like it!
                a: What do you think? Great choise, isn't it? 
                a: What an idea! 
        go!: /Satisfaction 
            
    state: Satisfaction
        buttons:
            "Another activity!" -> /activityType
            "Cool, thanks!" -> /GoodBye
            
    state: GoodBye
        a: I was happy to help you. Hope you'll be back soon :)
        
        
        
