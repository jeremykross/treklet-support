(ns treklet-support.petfighter
  (:require [treklet-support.css-util :as css-util]
            [cssgen :as css]))

(defn sprite-css
  []
  (css/css
    [".petfighter-sprite"
     :position :absolute
     :width "40px"
     :height "40px"
     :image-rendering "-moz-crisp-edges"
     :image-rendering "-webkit-optimize-contrast"
     :image-rendering "optimize-contrast"
     :-ms-interpolation-mode "nearest-neighbor"]))
