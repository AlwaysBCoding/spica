(ns spica.protocol.instance)

(defprotocol SpicaInstanceProtocol
  ""
  (serialize [self serializer])
  (data [self data-fn])
  (destroy [self] [self connection])
  (revise [self params] [self params connection])
  (id [self])
  (manifest [self])
  (attr [self attribute]))
