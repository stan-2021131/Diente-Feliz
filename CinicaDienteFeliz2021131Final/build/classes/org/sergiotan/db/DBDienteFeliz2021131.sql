Drop database if exists DBDienteFeliz2021131;
Create database DBDienteFeliz2021131;

Use DBDienteFeliz2021131;

Create table Pacientes(
	codigoPaciente Int not null,
    nombrePaciente Varchar(50) not null,
    apellidosPaciente Varchar(50) not null,
    sexo char not null,
    fechaNacimiento Date not null,
    direccionPaciente Varchar(100) not null,
    telefonoPersonal Varchar(8) not null,
    fechaPrimeraVisita Date,
    Primary Key PK_codigoPaciente(codigoPaciente)
);

Create table Especialidades(
	codigoEspecialidad int not null auto_increment,
    descripcion Varchar(100)not null,
    primary key PK_codigoEspecialidad(codigoEspecialidad)
);

Create table Medicamentos(
	codigoMedicamento int not null auto_increment,
    nombreMedicamento Varchar(100) not null,
    primary key PK_codigoMedicamento(codigoMedicamento)
);

Create table Doctores(
	numeroColegiado Int not null,
    nombresDoctor Varchar(50) not null,
    apellidosDoctor Varchar(50) not null,
    telefonoCOntacto Varchar(8) not null,
    codigoEspecialidad int not null,
    primary key PK_numeroColegiado(numeroColegiado),
    constraint FK_Doctores_Especialidades 
		foreign key (codigoEspecialidad) references Especialidades(codigoEspecialidad)
);

Create table Recetas(
	codigoReceta int not null auto_increment,
    fechaRecete Date not null,
    numeroColegiado int not null,
    primary key PK_codigoReceta(codigoReceta),
    constraint FK_Recetas_Doctores foreign key (numeroColegiado)
		references Doctores(numeroColegiado)
);

Create table Citas(
	codigoCita int not null auto_increment,
    fechaCita date not null,
    horaCita time not null,
    tratamiento varchar(150) not null,
    descripConActual varchar(255) not null,
    codigoPaciente int not null,
    numeroColegiado int not null,
    primary key PK_codigoCita(codigoCita),
    constraint FK_Citas_Pacientes foreign key(codigoPaciente)
		references Pacientes(codigoPaciente),
	constraint FK_Citas_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);

Create table DetalleReceta(
	codigoDetalleReceta int not null auto_increment,
    dosis varchar(100) not null,
    codigoReceta int not null,
    codigoMedicamento int not null,
    primary key PK_codigoDetalleReceta(codigoDetalleReceta),
    constraint FK_DetalleReceta_Recetas foreign key (codigoReceta)
		references Recetas(codigoReceta),
	constraint FK_DetalleReceta_Medicamentos foreign key (codigoMedicamento)
		references Medicamentos(codigoMedicamento)
);

-- ------------------------------------------------------------------------------------------------------------
-- ----------PROCEDIMIENTOS ALMACENADOS
-- ----------PACIENTES-------------------------------------------------------------------------
-- Agregar Paciente
Delimiter //
Create procedure sp_AgregarPaciente(in codigoPaciente int, in nombrePaciente varchar(50), in apellidosPaciente varchar(50), in sexo char,
	in fechaNacimiento date, in direccionPaciente varchar(100), in telefonoPersonal varchar(8), in fechaPrimeraVisita date)
    Begin
		Insert into Pacientes(codigoPaciente, nombrePaciente,apellidosPaciente,sexo,fechaNacimiento,direccionPaciente,telefonoPersonal,fechaPrimeraVisita)
			values(codigoPaciente, nombrePaciente,apellidosPaciente,sexo,fechaNacimiento,direccionPaciente,telefonoPersonal,fechaPrimeraVisita);
    End //
Delimiter ;
Call sp_AgregarPaciente(1001, 'Sergio Estuardo', 'Tan Coromac', 'M', '2004-10-21', 'Zona 1 Mixco', '50156315',now());
Call sp_AgregarPaciente(1002, 'Adriana Jimena', 'Tan Coromac', 'F', '2014-7-16', 'Zona 1 Mixco', '54866898',now());
Call sp_AgregarPaciente(1003, 'Angeles', 'Paz Avila', 'M', '1999-4-25', 'Quiché', '47963205',now());
Call sp_AgregarPaciente(1004, 'Lara Emilia', 'Nuñez Arias', 'F', '2000-9-1', 'Petén', '47632105',now());
Call sp_AgregarPaciente(1005, 'Alma', 'Vela Ocaña', 'F', '2020-1-17', 'Cobán', '74230540',now());
Call sp_AgregarPaciente(1006, 'Martin', 'Amado Martorell', 'M', '1983-6-30', 'El Tejar', '45023105',now());
Call sp_AgregarPaciente(1007, 'Regina  Montserrat', 'Sanchez Lorenzo', 'F', '2008-10-10', 'Patzun', '10245760',now());
Call sp_AgregarPaciente(1008, 'Maximo  Bernardino', 'Adan Merchan', 'M', '2005-11-21', 'Chajul', '75012364',now());
Call sp_AgregarPaciente(1009, 'Diana Jimena', 'Castillo Cañete', 'F', '1980-12-10', 'Ciudad de Guatemala', '49630210',now());
Call sp_AgregarPaciente(1010, 'Humberto Bautista', 'Rios Olmedo', 'M', '2016-2-12', 'Itzapa', '56021354',now());
-- Listar Pacientes
Delimiter //
	Create procedure sp_ListarPacientes()
		begin
			Select
				P.codigoPaciente,
                P.nombrePaciente,
                P.apellidosPaciente,
                P.sexo,
                P.fechaNacimiento,
                P.direccionPaciente,
                P.telefonoPersonal,
                P.fechaPrimeraVisita
			From Pacientes As P;
        End//
Delimiter ;
-- Call sp_ListarPacientes;

-- Buscar Paciente
Delimiter //
	Create procedure sp_BuscarPaciente (in codPaciente int)
		begin
			Select
				P.codigoPaciente, 
                P.nombrePaciente,
                P.apellidosPAciente,
                P.sexo,
                P.fechaNacimiento,
                P.direccionPaciente,
                P.telefonoPersonal,
                P.fechaPrimeraVisita
			From Pacientes As P where P.codigoPaciente = codPaciente;
        end//
Delimiter ;
-- Call sp_BuscarPaciente(1001);

-- EliminarPaciente
Delimiter //
	Create procedure sp_EliminarPaciente (in codPaciente int)
		begin
			delete from Pacientes
				where codigoPaciente = codPaciente;
        end//
Delimiter ;



-- Editar Paciente
Delimiter //
	Create procedure sp_EditarPaciente(in codPaciente int, in nombPaciente varchar(50), in apPaciente varchar(50), in sx char, in fechNac date, 
		in direcPaciente varchar(100), in telPersonal varchar(8), in fechPrimVisit date)
        begin
			Update Pacientes
				set
                nombrePaciente = nombPaciente,
                apellidosPaciente = apPaciente,
                sexo = sx,
                fechaNacimiento = fechNac,
                direccionPaciente = direcPaciente,
                telefonoPersonal = telPersonal,
                fechaPrimeraVisita = fechPrimVisit
				where codigoPaciente = codPaciente;
        end//
Delimiter ;
-- Call sp_EditarPaciente (1001, 'Mario', 'Tan', 'M', '2008-10-10','Zona 1 Mixco', '54866898', now());


-- Empezar a crear
-- ---------------------------------------------------ESPECIALIDADES------------------------------------------------------------------------------------------
-- Agregar Especialidad
Delimiter //
	Create procedure sp_AgregarEspecialidad(in descripcion varchar(100))
		Begin
			Insert into Especialidades(descripcion)
				values(descripcion);
        End//
Delimiter ;
Call sp_AgregarEspecialidad('Odontopediatra');
Call sp_AgregarEspecialidad('Endodoncista');
Call sp_AgregarEspecialidad('Ortodoncista');
Call sp_AgregarEspecialidad('Periodoncista');
Call sp_AgregarEspecialidad('Odontólogo general');
Call sp_AgregarEspecialidad('Cirujano oral');
Call sp_AgregarEspecialidad('Prostodoncista');
Call sp_AgregarEspecialidad('Anestesiología dental');
Call sp_AgregarEspecialidad('Radiología oral');
Call sp_AgregarEspecialidad('Patólogo oral');

-- Listar Especialidades
Delimiter //
	Create procedure sp_ListarEspecialidades()
		Begin
			Select E.codigoEspecialidad,
				E.descripcion
			from Especialidades as E;
        End//
Delimiter ;
-- Call sp_ListarEspecialidades();

-- Buscar Especialidad
Delimiter //
	Create procedure sp_BuscarEspecialidad(in codEsp int)
		Begin 
			Select E.codigoEspecialidad,
				E.descripcion
			from Especialidades as E where E.codigoEspecialidad = codEsp;			
        End//
Delimiter ;
-- Call sp_BuscarEspecialidad(2);

-- Eliminar Especialidad
Delimiter //
	Create procedure sp_EliminarEspecialidad(in codEsp int)
		Begin
			Delete from Especialidades
				where codigoEspecialidad = codEsp;
        End//
Delimiter ;

-- Editar Especialidad 
Delimiter //
	Create procedure sp_EditarEspecialidad(in codEsp int, in descr varchar(100))
		Begin
			Update Especialidades
            set 
				descripcion = descr
			where codigoEspecialidad = codEsp;
        End//
Delimiter ;
-- Call sp_EditarEspecialidad(1, 'Prostodoncista');

-- -------------------------------------------------------MEDICAMENTOS-------------------------------------------------------------------------------------------
-- Agregar Medicamentos
Delimiter //
	Create procedure sp_AgregarMedicamento(in nombreMedicamento varchar (100))
		Begin 
			Insert into Medicamentos(nombreMedicamento)
				values (nombreMedicamento);
        End//
Delimiter ;
Call sp_AgregarMedicamento('Pasta Dental');
Call sp_AgregarMedicamento('Amoxicilina ');
Call sp_AgregarMedicamento('Hilo Dental');
Call sp_AgregarMedicamento('Cefalexina');
Call sp_AgregarMedicamento('Penicilina');
Call sp_AgregarMedicamento('Tetraciclina');
Call sp_AgregarMedicamento('Azitromicina');
Call sp_AgregarMedicamento('Clindamicina');
Call sp_AgregarMedicamento('Piperacilina ');
Call sp_AgregarMedicamento('Eritromicina');

-- Listar Medicamentos
Delimiter //
	Create procedure sp_ListarMedicamentos()
		Begin
			Select
				M.codigoMedicamento,
                M.nombreMedicamento
			from Medicamentos as M;
        End//
Delimiter ;
-- Call sp_ListarMedicamentos();

-- Buscar Medicamento
Delimiter //
	Create procedure sp_BuscarMedicamento(in codMed int)
		Begin
			Select
				M.codigoMedicamento,
                M.nombreMedicamento
			from Medicamentos as M where M.codigoMedicamento = codMed;			
		End//
Delimiter ;

-- Eliminar Medicamento
Delimiter //
	Create procedure sp_EliminarMedicamento(in codMed int)
		Begin
			Delete from Medicamentos
				where codigoMedicamento = codMed;
        End// 
Delimiter ;

-- Editar Medicamento
Delimiter //
	Create procedure sp_EditarMedicamento(in codMed int, in nomMed varchar(100))
		Begin 
			Update Medicamentos
            set 
				nombreMedicamento = nomMed
			where codigoMedicamento = codMed;
        End//
Delimiter ;

-- -----------------------------------------DOCTORES----------------------------------------------------------------------------------------------------
-- Agregar Doctor
Delimiter //
	Create procedure sp_AgregarDoctor(in numeroColegiado int, in nombresDoctor varchar(50), in apellidosDoctor varchar(50), telefonoCOntacto varchar(8), in codigoEspecialidad int)
		Begin 
			Insert Into Doctores(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoCOntacto, codigoEspecialidad)
				Values(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoCOntacto, codigoEspecialidad);
        End//
Delimiter ;
Call sp_AgregarDoctor(225467536, 'Jose', 'Martínez', 50236478, 2);
Call sp_AgregarDoctor(452103697, 'Octavio', 'Jara', 4520136, 10);
Call sp_AgregarDoctor(485210236, 'Flor', 'Liu', 4875620, 5);
Call sp_AgregarDoctor(745691203, 'Aurea', 'Rus', 30214569, 1);
Call sp_AgregarDoctor(789541207, 'Teodoro', 'Andrade', 12368510, 3);
Call sp_AgregarDoctor(360214587, 'Andrés', 'Colomer', 3021456, 7);
Call sp_AgregarDoctor(987456324, 'Víctor ', 'Tena', 98745201, 9);
Call sp_AgregarDoctor(750210745, 'Monserrat', 'Galindo', 98764520, 7);
Call sp_AgregarDoctor(362014854, 'Myriam ', 'Mejias', 30264795, 4);
Call sp_AgregarDoctor(451557896, 'Emilio', 'Lazaro', 15320467, 6);

-- Listar Doctores
Delimiter //
	Create procedure sp_ListarDoctores()
		Begin 
			Select
				D.numeroColegiado,
                D.nombresDoctor,
                D.apellidosDoctor,
                D.telefonoCOntacto,
                D.codigoEspecialidad
			from Doctores as D;
        End//
Delimiter ;
-- Call sp_ListarDoctores();

-- Buscar Doctor
Delimiter //
	Create procedure sp_BuscarDoctor(in numCol int)
		Begin 
			Select
				D.numeroColegiado,
                D.nombresDoctor,
                D.apellidosDoctor,
                D.telefonoCOntacto,
                D.codigoEspecialidad
			from Doctores as D where D.numeroColegiado = numCol;			
        End//
Delimiter ;

-- Eliminar Doctor
Delimiter //
	Create procedure sp_EliminarDoctor(in numCol int)
		Begin 
			Delete from Doctores
				where numeroColegiado = numCol;
        End//
Delimiter ;

-- Editar Doctor
Delimiter //
	Create procedure sp_EditarDoctor(in numCol int,in nomDoc varchar(50), in apeDoc varchar(50), in telPer varchar(8))
		Begin
			Update Doctores
				set nombresDoctor = nomDoc,
					apellidosDoctor = apeDoc,
                    telefonoCOntacto = telPer
				where numeroColegiado = numCol;
        End//
Delimiter ;
-- Call sp_EditarDoctor(225467536, 'Juan', 'Ramirez', '79632015');

-- ------------------------------------------------RECETAS--------------------------------------------------------------------------------
-- Agregar Receta
Delimiter //
	Create procedure sp_AgregarReceta (in fechaRecete date, in numeroColegiado int)
		Begin
			Insert into Recetas(fechaRecete, numeroColegiado)
				values(fechaRecete, numeroColegiado);
        End//
Delimiter ;
Call sp_AgregarReceta(now(), 225467536);
Call sp_AgregarReceta(now(), 745691203);
Call sp_AgregarReceta(now(), 452103697);
Call sp_AgregarReceta(now(), 451557896);
Call sp_AgregarReceta(now(), 362014854);
Call sp_AgregarReceta(now(), 485210236);
Call sp_AgregarReceta(now(), 789541207);
Call sp_AgregarReceta(now(), 360214587);
Call sp_AgregarReceta(now(), 987456324);
Call sp_AgregarReceta(now(), 750210745);

-- Listar Recetas
Delimiter //
	Create procedure sp_ListarRecetas()
		Begin
			Select R.codigoReceta,
				R.fechaRecete,
                R.numeroColegiado
			from Recetas as R;
        End//
Delimiter ;
-- Call sp_ListarRecetas();

-- Buscar Receta
Delimiter //
	Create procedure sp_BuscarReceta(in codRec int)
		Begin
			Select R.codigoReceta,
				R.fechaRecete,
                R.numeroColegiado
			from Recetas as R where R.codigoReceta = codRec;			
        End//
Delimiter ;
-- Call sp_BuscarReceta(3);

-- Eliminar Receta
Delimiter //
	Create procedure sp_EliminarReceta(in codRec int)
		Begin 
			Delete from Recetas
				where codigoReceta = codRec;
        End//
Delimiter ;

-- Editar Receta
Delimiter //
	Create procedure sp_EditarReceta(in codRec int ,in fechRec date)
		Begin
			Update Recetas
				set fechaRecete = fechRec
                where codigoReceta = codRec;
        End//
DElimiter ;
-- Call sp_EditarReceta(3, '2021-05-10');

-- -----------------------------------------------------CITAS-----------------------------------------------------------------------------------------------
-- Agregar Cita
Delimiter //
	Create procedure sp_AgregarCita(in fechaCita date,in  horaCita time,in tratamiento varchar(100),in descripConActual varchar(255), in codigoPaciente int, in numeroColegiado int)
		Begin
			Insert Into Citas(fechaCita, horaCita, tratamiento, descripConActual, codigoPaciente, numeroColegiado)
				Values(fechaCita, horaCita, tratamiento, descripConActual, codigoPaciente, numeroColegiado);
        End//
Delimiter ;
Call sp_AgregarCita(now(), now(), 'Blanqueamiento', 'Estable', 1002, 225467536);
Call sp_AgregarCita('2022-06-25', now(), 'Implante', 'Estable', 1003, 452103697);
Call sp_AgregarCita('2022-07-15', now(), 'Prótesis', 'Moderado', 1010, 362014854);
Call sp_AgregarCita(now(), now(), 'Extracción', 'Crítico', 1004, 451557896);
Call sp_AgregarCita('2022-09-1', now(), 'Empaste', 'Estable', 1009, 750210745);
Call sp_AgregarCita('2022-10-10', now(), 'Endodoncia', 'Moderado', 1001, 987456324);
Call sp_AgregarCita('2022-07-21', now(), 'ortodoncia', 'Estable', 1005, 360214587);
Call sp_AgregarCita('2022-06-20', now(), 'Implante', 'Crítico', 1008, 485210236);
Call sp_AgregarCita('2022-11-4', now(), 'Blanqueamiento', 'Moderado', 1007, 745691203);
Call sp_AgregarCita('2022-12-25', now(), 'Extraccion', 'Estable', 1006, 789541207);

-- Listar Citas
Delimiter //
	Create procedure sp_ListarCitas()
		Begin
			Select 
				C.codigoCita,
                C.fechaCita,
                C.horaCita,
                C.tratamiento,
                C.descripConActual,
                C.codigoPaciente,
                C.numeroColegiado
			from Citas as C;
        End//
Delimiter ;
-- Call sp_ListarCitas();

-- Buscar Cita
Delimiter //
	Create procedure sp_BuscarCita(in codCit int)
		Begin
			Select 
				C.codigoCita,
                C.fechaCita,
                C.horaCita,
                C.tratamiento,
                C.descripConActual,
                C.codigoPaciente,
                C.numeroColegiado
			from Citas as C where C.codigoCita = codCit;			
        End//
DElimiter ;
-- Call sp_BuscarCita(1);

-- Eliminar Cita
Delimiter //
	Create procedure sp_EliminarCita(in codCit int)
		Begin
			Delete from Citas
				where codigoCita = codCit;
        End//
Delimiter ;

-- Editar Cita
Delimiter //
	Create procedure sp_EditarCita(in codCit int, in fechCit date, in horCit time,in trat varchar(150), in descrAc varchar(255))
		Begin 
			Update Citas
				set fechaCita = fechCit,
					horaCita = horCit,
                    tratamiento = trat,
                    descripConActual = descrAc
				where codigoCita= codCit;
        End//
Delimiter ;

-- Call sp_EditarCita(1, '2022-05-22', '10:30:10', 'Extracción de diente', 'Moderado');

-- --------------------------------------------------DETALLE RECETA--------------------------------------------------------------------------------------
-- Agregar Detalle Receta
Delimiter //
	Create procedure sp_AgregarDetalle(in dosis varchar(100), in codigoReceta int, in codigoMedicamento int)
		Begin
			Insert Into DetalleReceta(dosis, codigoReceta, codigoMedicamento)
				Values (dosis, codigoReceta, codigoMedicamento);
        End//
DElimiter ;

Call sp_AgregarDetalle('10mg', 1, 2);
Call sp_AgregarDetalle('500mg', 2, 10);
Call sp_AgregarDetalle('30mg', 8, 4);
Call sp_AgregarDetalle('5mg', 9, 7);
Call sp_AgregarDetalle('3 veces al dia', 3, 1);
Call sp_AgregarDetalle('Cada noche', 4, 9);
Call sp_AgregarDetalle('Al amanecer', 6, 3);
Call sp_AgregarDetalle('Despues de cada comida', 7, 5);
Call sp_AgregarDetalle('80mg', 5, 6);
Call sp_AgregarDetalle('100mg', 10, 8);

-- Listar Detalle Recetas
Delimiter //
	Create procedure sp_ListarDetalles()
		Begin
			Select
				N.codigoDetalleReceta,
				N.dosis,
                N.codigoReceta,
                N.codigoMedicamento
			from DetalleReceta as N;
        End//
Delimiter ;
-- Call sp_ListarDetalles();

-- Buscar Detalle
Delimiter //
	Create procedure sp_BuscarDetalle(in codDet int)
		Begin
			Select
				N.codigoDetalleReceta,
				N.dosis,
                N.codigoReceta,
                N.codigoMedicamento
			from DetalleReceta as N where N.codigoDetalleReceta = codDet;			
        End //
Delimiter ;

-- Eliminar Detalle
Delimiter //
	Create procedure sp_EliminarDetalle(in codDet int)
		Begin
			Delete from DetalleReceta
				where codigoDetalleReceta = codDet;
        End//
Delimiter ;

-- Editar Detalle
Delimiter //
	Create procedure sp_EditarDetalle(in codDet int, in dosi varchar(100))
		Begin
			Update DetalleReceta
				set dosis = dosi
			where codigoDetalleReceta = codDet;
        End //
DElimiter ;


-- ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'admin';

-- -------------------------------------------------------LOGIN--------------------------------------------------
Create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50)not null,
    contrasena varchar(50)not null,
    primary key PK_codigoUsuario(codigoUsuario)
);

Delimiter //
	Create procedure sp_AgregarUsuario(in nombreUsuario varchar(100),in apellidoUsuario varchar(100), in usuarioLogin varchar(100), in contrasena varchar(100))
    Begin
		Insert into Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
			values(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
	End //
Delimiter ;

Delimiter //
	Create procedure sp_ListarUsuarios()
    Begin
		Select U.codigoUsuario, U.nombreUsuario, U.apellidoUsuario, U.usuarioLogin, U.contrasena
			from Usuario as U;
    End //
Delimiter ;

Call sp_AgregarUsuario('Sergio', 'Tan', 'etan', '4010');
Call sp_AgregarUsuario('José', 'Ramirez', 'mRam', '7531');

-- Call sp_ListarUsuarios();

Create table Login(
	usuarioMaster varchar(100) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster(usuarioMaster)
);

