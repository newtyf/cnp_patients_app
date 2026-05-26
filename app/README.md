Estructura de carpetas

```markdown
app/
├── data/
│   ├── db/
│   │   ├── DatabaseHelper.java          ← SQLiteOpenHelper único
│   │   └── DatabaseConstants.java       ← nombres de tablas y columnas
│   ├── model/
│   │   ├── Usuario.java
│   │   ├── Paciente.java
│   │   ├── Consulta.java
│   │   ├── Dieta.java
│   │   ├── Alimento.java
│   │   ├── Comida.java                  ← desayuno, almuerzo, cena...
│   │   └── Nutricionista.java
│   └── repository/
│       ├── PacienteRepository.java
│       ├── ConsultaRepository.java
│       ├── DietaRepository.java
│       ├── AlimentoRepository.java
│       └── AuthRepository.java
│
├── presentation/
│   │
│   ├── auth/                            ← sin bottom nav
│   │   ├── login/
│   │   │   ├── LoginActivity.java
│   │   │   ├── LoginPresenter.java
│   │   │   └── LoginContract.java
│   │   ├── registro/
│   │   │   ├── RegistroActivity.java
│   │   │   ├── RegistroPresenter.java
│   │   │   └── RegistroContract.java
│   │   └── pin/
│   │       ├── PinActivity.java
│   │       ├── PinPresenter.java
│   │       └── PinContract.java
│   │
│   ├── main/                            ← contenedor del bottom nav
│   │   └── MainActivity.java            ← host de fragments con bottom nav
│   │
│   ├── dashboard/
│   │   ├── DashboardFragment.java
│   │   ├── DashboardPresenter.java
│   │   └── DashboardContract.java
│   │
│   ├── paciente/
│   │   ├── lista/
│   │   │   ├── PacienteListFragment.java
│   │   │   ├── PacienteListPresenter.java
│   │   │   └── PacienteListContract.java
│   │   ├── detalle/
│   │   │   ├── PacienteDetalleFragment.java  ← contiene tab de consultas
│   │   │   ├── PacienteDetallePresenter.java
│   │   │   └── PacienteDetalleContract.java
│   │   ├── creacion/
│   │   │   ├── PacienteCreacionFragment.java
│   │   │   ├── PacienteCreacionPresenter.java
│   │   │   └── PacienteCreacionContract.java
│   │   └── edicion/
│   │       ├── PacienteEdicionFragment.java
│   │       ├── PacienteEdicionPresenter.java
│   │       └── PacienteEdicionContract.java
│   │
│   ├── consulta/
│   │   ├── lista/
│   │   │   ├── ConsultaListFragment.java     ← va dentro de detalle paciente
│   │   │   ├── ConsultaListPresenter.java
│   │   │   └── ConsultaListContract.java
│   │   ├── registro/
│   │   │   ├── ConsultaRegistroActivity.java ← Activity propia por los steps
│   │   │   ├── step1/
│   │   │   │   ├── ConsultaStep1Fragment.java
│   │   │   │   ├── ConsultaStep1Presenter.java
│   │   │   │   └── ConsultaStep1Contract.java
│   │   │   └── step2/
│   │   │       ├── ConsultaStep2Fragment.java
│   │   │       ├── ConsultaStep2Presenter.java
│   │   │       └── ConsultaStep2Contract.java
│   │   └── detalle/
│   │       ├── ConsultaDetalleFragment.java
│   │       ├── ConsultaDetallePresenter.java
│   │       └── ConsultaDetalleContract.java
│   │
│   ├── dieta/
│   │   ├── lista/
│   │   │   ├── DietaListFragment.java
│   │   │   ├── DietaListPresenter.java
│   │   │   └── DietaListContract.java
│   │   ├── creacion/
│   │   │   ├── DietaCreacionActivity.java    ← Activity propia por complejidad
│   │   │   ├── DietaCreacionPresenter.java
│   │   │   └── DietaCreacionContract.java
│   │   └── alimentos/
│   │       ├── AlimentoSelectorFragment.java ← navegador + selección + cantidades
│   │       ├── AlimentoSelectorPresenter.java
│   │       └── AlimentoSelectorContract.java
│   │
│   ├── mapa/
│   │   ├── MapaFragment.java
│   │   ├── MapaPresenter.java
│   │   └── MapaContract.java
│   │
│   └── nutricionista/
│       ├── detalle/
│       │   ├── NutricionistaDetalleFragment.java
│       │   ├── NutricionistaDetallePresenter.java
│       │   └── NutricionistaDetalleContract.java
│       └── edicion/
│           ├── NutricionistaEdicionFragment.java
│           ├── NutricionistaEdicionPresenter.java
│           └── NutricionistaEdicionContract.java
│
├── common/
│   ├── base/
│   │   ├── BaseActivity.java             ← lógica común a todas las activities
│   │   ├── BaseFragment.java             ← lógica común a todos los fragments
│   │   └── BasePresenter.java            ← onDestroy, null checks comunes
│   ├── ui/
│   │   ├── adapters/
│   │   │   ├── PacienteAdapter.java
│   │   │   ├── ConsultaAdapter.java
│   │   │   ├── DietaAdapter.java
│   │   │   └── AlimentoAdapter.java
│   │   └── dialogs/
│   │       ├── ConfirmDialog.java        ← popup reutilizable
│   │       └── MenuPopup.java            ← los diferentes menús con popup
│   └── utils/
│       ├── DateUtils.java
│       ├── ValidationUtils.java
│       └── SessionManager.java           ← guardar sesión del usuario
│
└── NutricionistaApp.java                 ← Application class
```