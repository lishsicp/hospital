window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        //Uncomment Below to persist sidebar toggle between refreshes
        let on = "bi-toggle-on";
        let off = "bi-toggle-off";
        let toggleIconClassList = document.getElementById("menuToggle").classList;
        if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
            document.body.classList.toggle('sb-sidenav-toggled');
        }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
            if (toggleIconClassList.contains(on)) {
                toggleIconClassList.remove(on);
                toggleIconClassList.add(off);
            } else if (toggleIconClassList.contains(off)) {
                toggleIconClassList.remove(off);
                toggleIconClassList.add(on);
            }
        });
    }

});

function deleteInvalid(input) {
    input.classList.remove("is-invalid");
}