(ns treklet-support.core
  (:require [cssgen :as css]
            [treklet-support.css-util :as css-util]))

(def easing-0 "cubic-bezier(0.215, 0.610, 0.355, 1.000)")

(def vendor-prefixes ["-moz-" "-webkit-"])

(defn ^:dynamic vendor-prefix
  [prop value]
  (reduce (fn [l p] (conj l (list (keyword (str p (name prop))) (name value)))) () vendor-prefixes))

(defn anim-definition
  ([anim-name] (anim-definition anim-name ".5s"))
  ([anim-name duration] (anim-definition anim-name duration "ease-in-out"))
  ([anim-name duration timing-func]
   (list
     (vendor-prefix :animation-name anim-name)
     (vendor-prefix :animation-duration duration)
     (vendor-prefix :animation-timing-function timing-func))))

(defn keyframed
  ([anim-name css] 
   (reduce (fn [a prefix] (str a "\n" (keyframed anim-name css prefix))) (String.) vendor-prefixes))
  ([anim-name css vendor]
  (letfn [(wrap [vendor css]
            (.replaceAll (str "@" vendor "keyframes " anim-name " {\n" css "\n}") "VENDOR" vendor))]
    (if vendor
      (wrap vendor css)))))

(defmacro keyframes
  [anim-name & forms]
  `(binding [vendor-prefix 
            (fn [prop# value#]
              (list (keyword (str "VENDOR" (name prop#))) (name value#)))]
    (keyframed ~anim-name (css/css ~@forms))))


(defn main-css
  []
  (css/css
    [".treklet-plugin-div"
     :position :absolute
     :width "100% !important"
     :height "!00% !important"
     :top "0px"
     :left "0px"
     :overflow :visible
     :z-index "16000000"
     :pointer-events :none]
    [".treklet-plugin-div > div"
     :width "100%"
     :height "100%"]
    ["body"
     :width "100%"
     :height "100%"]))

; height - 37
(defn chat-box-css
  []
  (let [chat-box-height 210]
    (css/css
      ["#treklet-chat-box"
       (vendor-prefix :transition "opacity .5s")
       (vendor-prefix :transform "scale(1.02,1.02)")
       :position :fixed
       :bottom "33px"
       :left "22.5%"
       :width "55%"
       :padding "0"
       :height (str chat-box-height "px")
       :overflow :hidden
       ["#treklet-chat-window"
        (vendor-prefix :transform "translateZ(0)")
        (vendor-prefix :transition "top .5s")
        :position :relative
        :padding "0"
        :top (str (- chat-box-height 39) "px")]]
      [".treklet-chat-line"
       (vendor-prefix :transform "translateZ(0)")
       :margin-bottom "5px"
       :padding-bottom "5px"
       :width "100%"
       :overflow :hidden
       ["img, .treklet-line-phrase"
        (vendor-prefix :transition "all .55s")]
       ["img"
        :border-radius "4px"
        :float :left
        :margin-left "0px"
        :margin-bottom "0px"
        :width "0px"
        :height "32px"]
       [".treklet-line-phrase"
        :color "black"
        :background-color "white"
        :position :relative
        :border-radius "4px"
        :font-family "tahoma, geneva, sans-serif"
        :font-size "15px"
        :line-height "18px"
        :z-index   "16000004"
        :margin    "0px 0px 0px 0px"
        :padding   "5px 14px 5px 16px"]]
      [".treklet-chat-line.initial-state"
       ["img"
        :margin-left "15px"
        :width "32px"]
       [".treklet-line-phrase"
        :line-height "22px"
        :margin-right "15px"
        :margin-left  "55px"]])))

;Complete
(defn speech-input-css 
  []
  (css/css
    ["#treklet-speech-input.inplace"
     :bottom "40px !important"]
    ["#treklet-speech-input"
     (vendor-prefix :transition "all .5s") ;"bottom .5s, opacity .45s, background .45s, color .45s, background-color .45s")
     :position :fixed
     :bottom "-100px"
     :left "22.5%"
     :width "55%"
     :padding "0"
     :margin "0"
     :z-index "16000003"
    ["input"
     (vendor-prefix :transition "all .3s") ;"bottom .5s, opacity .45s, background .45s, color .45s, background-color .45s")
     (vendor-prefix :box-sizing "border-box")
     :border-radius "25px"
     :font-family "tahoma, geneva, sans-serif"
     :font-size "15px"
     :padding "5px 14px 5px 14px"
     :width "100%"
     :margin "0"
     :background-color "rgba(0,0,0,.05)"
     :opacity ".8"
     :border "1px solid lightgrey"
     (vendor-prefix :box-shadow "0px 4px 2px rgba(0,0,0,.7)")]
    ["input:focus"
     :border-style :none
     :outline :none
     :background :white 
     :color :black
     :border "1px solid lightgrey"
     :opacity "1"
     :border-radius "4px"
     ;(vendor-prefix :box-shadow "0px 0px 10px rgba(0,0,0,.5) inset")
     (vendor-prefix :transform "scale(1.02,1.02)")]]))

; This is complete
(defn avatar-anim-css []
  (str
    (keyframes "anim-pop"
               ["0%"   (vendor-prefix :transform "scale(1,0)")]
               ["25%"  (vendor-prefix :transform "scale(1,1.25)")]
               ["75%"  (vendor-prefix :transform "scale(1,.9)")] 
               ["100%" (vendor-prefix :transform "scale(1,1)")])
    (keyframes "anim-wither"
               ["0%" 
                :opacity 1
                (vendor-prefix :box-shadow "7px 7px 10px rgba(0,0,0,.7)")]
               ["100%" 
                :opacity 0
                (vendor-prefix :transform "translate(-10px, 10px)")
                (vendor-prefix :box-shadow "17px 17px 75px rgba(0,0,0,.7)")])
    (keyframes "anim-entrance"
               ["0%" (vendor-prefix :transform "scale(0, 1.75)")]
               ["25%" (vendor-prefix :transform "scale(1.75, 1.75)")]
               ["45%" (vendor-prefix :transform "scale(1.75, 1.75)")]
               ["100%" (vendor-prefix :transform "scale(1, 1)")])
    (keyframes "anim-exit"
               ["0%" (vendor-prefix :transform "scale(1,1)")]
               ["100%" 
                (vendor-prefix :transform "scale(0,0)")
                :opacity 0])
    (keyframes "anim-notify"
               ["0%" 
                (vendor-prefix :transform "scale(0,0)")
                :opacity 0]
               ["100%"
                (vendor-prefix :transform "scale(1,1)")
                :opacity ".75"])))

;complete
(defn avatar-css []
  (css/css
    [".anim-pop"
     (anim-definition "anim-pop" ".4s" "ease-out")]
    [".anim-wither"
     (anim-definition "anim-wither" "1s" easing-0)]
    [".anim-entrance"
     (anim-definition "anim-entrance" ".4s")]
    [".anim-exit"
     (anim-definition "anim-exit" ".450s")]
    [".anim-notify"
     (anim-definition "anim-notify" ".450s")]
    [".anim-hide"
     (anim-definition "anim-hide" ".25s")]
    [".anim-show"
     (anim-definition "Anim-show" ".25s" "ease-in")]
    [".locale-changed-notice"
     :padding "20px"
     :background-color "#FBE94F"
     :border "2px solid #F0C36D"
     :color "#2E3436"
     :border-radius "5px"
     :opacity ".9" 
     :position :absolute
     :font-family "tahoma, geneva, sans-serif"
     :font-size "15px"
     :z-index "16000002"
     (vendor-prefix :transition "opacity .450s ease-in-out")
     (vendor-prefix :box-shadow "0px 0px 10px rgba(0,0,0,.7)")]
    ["div.avatar"
     :position :absolute
     :top "40.45px"
     :left "25px"
     :overflow "visible"
      :z-index "16000000"
     (vendor-prefix :transition "top .5s cubic-bezier(0.215, 0.610, 0.355, 1.000), left .5s cubic-bezier(0.215, 0.610, 0.355, 1.000)") 
     ["img"
      :width "64px"
      :height "64px"
      :display :block
      :margin-left :auto
      :margin-right :auto
      :border-radius "4px"
      :position "relative"
      :z-index "16000000"
      (vendor-prefix :box-shadow "3px 3px 3px rgba(0,0,0,.7)")]
     ["span.avatar-name"
      :margin "5px 0px 0px 0px"
      :padding "5px 8px 5px 8px"
      :font-size "14px" 
      :font-family "tahoma, geneva, sans-serif"
      :color :white
      :background-color :cornflowerblue
      :text-align :center
      :white-space "nowrap"
      :border-radius "2px"
      :position "relative"
      :z-index "16000000"
      :display "block"
      (vendor-prefix :box-shadow "2px 2px 3px rgba(0,0,0,.7)")]
     ["div.avatar-balloon"
      :background-color :red
      :padding "10px" 
      :font-size "15px" 
      :font-family "tahoma, geneva, sans-serif"
      :top "-40px"
      :left "0px"
      :width "200px"
      :border-radius "4px"
      :opacity "0"
      :display "none"
      :z-index "16000001"
      :overflow :hidden
      :position :absolute
      (vendor-prefix :box-shadow "7px 7px 10px rgba(0,0,0,.7)")]]))
