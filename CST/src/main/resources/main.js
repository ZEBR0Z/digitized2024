import MicroModal from 'micromodal';
var MicroModal = require('micromodal');


MicroModal.init();

import micromodal from "https://cdn.skypack.dev/micromodal@0.4.6";

// micromodal.init({
//     disableScroll: true
// });

MicroModal.init({
    onShow: modal => console.info(`${modal.id} is shown`), // [1]
    onClose: modal => console.info(`${modal.id} is hidden`), // [2]
    openTrigger: 'data-custom-open', // [3]
    closeTrigger: 'data-custom-close', // [4]
    openClass: 'is-open', // [5]
    disableScroll: true, // [6]
    disableFocus: true, // [7]
    awaitOpenAnimation: false, // [8]
    awaitCloseAnimation: false, // [9]
    debugMode: false // [10]
});
