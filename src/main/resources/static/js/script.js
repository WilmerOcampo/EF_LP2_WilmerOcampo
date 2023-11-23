
$('#form-habitacion').submit(function (e) {
    e.preventDefault();
    let form = '#form-habitacion'

    let numero = $('#numero').val();
    let tipo = $('#tipo').val();
    let precio = $('#precio').val();

    numero = parseInt(numero);
    precio = parseInt(precio);


    if (numero && tipo && precio && !isNaN(numero) && !isNaN(precio) && numero > 0 && precio > 0) {
        alertSuccess(form);
    } else {
        alertWarningEmpty(form);
    }
});

$('#form-usuario').submit(function (e) {
    e.preventDefault();
    let form = '#form-usuario'
    let nombre = $('#nombre').val();
    let email = $('#email').val();
    let telefono = $('#telefono').val();

    if (nombre && email && telefono) {
        alertSuccess(form);
    } else {
        alertWarningEmpty(form);
    }
});

$('#form-pagar').submit(function (e) {
    e.preventDefault();
    let form = '#form-pagar'
    alertConfirm(form);
});

function alertLogout(e){
    e.preventDefault();
    alertSuccess();
}
function alertConfirm(form) {
    Swal.fire({
        title: '¿Estás seguro?',
        text: "¿Quieres realizar esta operación?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, confirmar!',
        cancelButtonText: 'No, cancelar!'
    }).then((result) => {
        if (result.isConfirmed) {
            alertSuccess(form)
        }
    })
}

function alertSuccess(form) {
    Swal.fire({
        title: '¡Operación exitosa!',
        icon: 'success',
        confirmButtonText: 'Aceptar'
    }).then(function () {
        $(form).off('submit').submit();
    });
}

function alertWarningEmpty(form) {
    Swal.fire({
        title: 'Campos vacíos detectados',
        text: 'Por favor llene los campos requeridos antes de guardar',
        icon: 'warning',
        confirmButtonText: 'Aceptar'
    }).then(function () {
        $(form).off('submit').submit()
    });
}
