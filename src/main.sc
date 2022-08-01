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
            a: Hello! I am {{$injector.botName}}. I am always happy to find new activities for you :)
            a: Hi! My name is {{$injector.botName}}. It seems like you need some new ideas for today :)
            a: What's up! It's {{$injector.botName}}. Bored, huh?
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
            
    state: activityType || modal = true
        a: Choose which type of activity you would like to do: Education, Recreational, Social, DIY, Charity, Cooking, Relaxation, Music, Busywork.
        q: (Education / Recreational / Social / DIY / Charity / Cooking / Relaxation / Music / Busywork)
        go!: /Offer
        
        state: LocalCatchAll
            event: noMatch
            a: Please, write the word exact the same way as I've spelled you it in the offer list.
            go!: ..
            
    state: Offer 
        script:
            log(toPrettyString($parseTree));
            $session.type = $parseTree._Root;
        a: Great choice! Let's see what {{$session.type}} activity I can offer you.
        go!: /Play
        
    state: Play
        script: 
            $temp.task = getActivity($session.type);
        if: $temp.task
            random:
                a: Look what idea I've found for you! {{$temp.task.activity}}. I hope you like it!
                a: {{$temp.task.activity}}. What do you think? Cool activity, isn't it? 
                a: {{$temp.task.activity}}. What an idea! 
        go!: /Satisfaction 
            
    state: Satisfaction
        buttons:
            "Another activity!" -> /activityType
            "Cool, thanks!" -> /GoodBye
            
    state: GoodBye
        a: I was happy to help you. Hope you'll be back soon :)
        q: $bye
    
