(ns treklet-support.css-util)

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

